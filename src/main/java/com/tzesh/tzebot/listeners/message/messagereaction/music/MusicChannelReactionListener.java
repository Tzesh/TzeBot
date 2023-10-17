package com.tzesh.tzebot.listeners.message.messagereaction.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.tzesh.tzebot.commands.music.*;
import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.command.CommandContextImpl;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.core.music.audio.GuildMusicManager;
import com.tzesh.tzebot.core.music.audio.PlayerManager;
import com.tzesh.tzebot.core.music.enums.MusicEmoteUnicodes;
import com.tzesh.tzebot.listeners.message.messagereaction.GenericMessageReactionEventListener;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is a simple class for handling music channel reactions
 *
 * @author tzesh
 */
public class MusicChannelReactionListener extends GenericMessageReactionEventListener<MessageReactionAddEvent> {
    private final MessageReactionAddEvent event;
    private final Long guildID;
    private final Long channelID;
    private final GuildChannel guildChannel;
    private final int currentVolume;
    private final UnicodeEmoji unicodeEmoji;
    private final List<String> args;
    private final AbstractMusicCommand<MessageReactionAddEvent> command;
    private final CommandContextImpl<MessageReactionAddEvent> commandContext;
    private final AudioManager audioManager;
    private final GuildMusicManager guildMusicManager;
    private final AudioPlayer audioPlayer;

    public MusicChannelReactionListener(MessageReactionAddEvent event) {
        this.event = event;
        this.guildID = event.getGuild().getIdLong();
        this.channelID = event.getChannel().getIdLong();
        this.guildChannel = Inventory.get(guildID);
        this.currentVolume = guildChannel.getVolume();
        this.audioManager = event.getGuild().getAudioManager();
        this.guildMusicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        this.audioPlayer = guildMusicManager.player;
        this.unicodeEmoji = event.getEmoji().asUnicode();
        this.args = initializeArgs(unicodeEmoji);
        this.commandContext = new CommandContextImpl<>(event, args, guildChannel);
        this.command = initializeCommand(unicodeEmoji);
    }

    private List<String> initializeArgs(UnicodeEmoji emoji) {
        List<String> arguments = new ArrayList<>();

        if (MusicEmoteUnicodes.volumeup.getUnicode().equals(emoji)) {
            arguments.add(String.valueOf(currentVolume + 25));
        } else if (MusicEmoteUnicodes.volumedown.getUnicode().equals(emoji)) {
            arguments.add(String.valueOf(currentVolume - 25));
        } else if (MusicEmoteUnicodes.next.getUnicode().equals(emoji)) {
            arguments.add("forward");
        } else if (MusicEmoteUnicodes.previous.getUnicode().equals(emoji)) {
            arguments.add("backward");
        }

        return arguments;
    }

    private AbstractMusicCommand<MessageReactionAddEvent> initializeCommand(UnicodeEmoji emoji) {
        if (MusicEmoteUnicodes.nowPlaying.getUnicode().equals(emoji)) {
            return audioPlayer.isPaused() ? new Resume<>() : new Pause<>();
        } else if (MusicEmoteUnicodes.stop.getUnicode().equals(emoji)) {
            return new Leave<>();
        } else if (MusicEmoteUnicodes.skip.getUnicode().equals(emoji)) {
            return new Skip<>();
        } else if (MusicEmoteUnicodes.volumeup.getUnicode().equals(emoji) || MusicEmoteUnicodes.volumedown.getUnicode().equals(emoji)) {
            return new Volume<>();
        } else if (MusicEmoteUnicodes.loop.getUnicode().equals(emoji)) {
            return new Loop<>();
        } else if (MusicEmoteUnicodes.queue.getUnicode().equals(emoji)) {
            return new Queue<>();
        } else if (MusicEmoteUnicodes.next.getUnicode().equals(emoji) || MusicEmoteUnicodes.previous.getUnicode().equals(emoji)) {
            return new Seek<>();
        } else if (MusicEmoteUnicodes.shuffle.getUnicode().equals(emoji)) {
            return new Shuffle<>();
        }

        return null;
    }

    @Override
    protected boolean canHandle(MessageReactionAddEvent event) {
        final boolean isBot = Objects.requireNonNull(event.getUser()).isBot();
        final boolean isChannel = guildChannel.doesMusicChannelExist() && channelID.equals(guildChannel.getBoundedMusicChannelID());
        final boolean isCommand = command != null;
        final boolean isUserInSameChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel() == audioManager.getConnectedChannel();

        return !isBot && isChannel && isCommand && isUserInSameChannel;
    }

    @Override
    protected void handle(MessageReactionAddEvent event) {
        boolean isUserInChannel = event.getMember().getVoiceState().getChannel() != null;

        removeReaction();
        if (!isUserInChannel) return;

        this.command.setBounded();
        this.command.handle(commandContext);
    }

    private void removeReaction() {
        event.getReaction().removeReaction(event.getUser()).queue();
    }
}

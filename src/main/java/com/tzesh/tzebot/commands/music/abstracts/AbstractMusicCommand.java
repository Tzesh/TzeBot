package com.tzesh.tzebot.commands.music.abstracts;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.core.command.CommandContextImpl;
import com.tzesh.tzebot.core.music.constants.MusicCommonConstants;
import com.tzesh.tzebot.core.music.audio.GuildMusicManager;
import com.tzesh.tzebot.core.music.audio.PlayerManager;
import com.tzesh.tzebot.core.music.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * An abstract class for music commands
 *
 * @author tzesh
 */
public abstract class AbstractMusicCommand<T extends GenericMessageEvent> extends AbstractCommand<T> {
    private final int DEFAULT_DELAY = MusicCommonConstants.DEFAULT_DELAY;
    protected boolean isBounded = false;
    protected int volume;
    protected PlayerManager playerManager;
    protected AudioManager audioManager;
    protected GuildVoiceState memberVoiceState;
    protected AudioChannel voiceChannel;
    protected GuildMusicManager musicManager;
    protected AudioPlayer audioPlayer;
    protected TrackScheduler scheduler;
    protected AudioTrackInfo audioTrackInfo;
    protected BlockingQueue<AudioTrack> queue;

    @Override
    protected void sendMessage(MessageEmbed message) {
        if (isBounded) {
            sendMessageAndDelete(message);
            return;
        }

        super.sendMessage(message);
    }

    @Override
    protected void initializeVariables(CommandContextImpl<T> commandContext) {
        super.initializeVariables(commandContext);
        this.playerManager = PlayerManager.getInstance();
        this.audioManager = guild.getAudioManager();
        this.memberVoiceState = member != null ? member.getVoiceState() : null;
        this.voiceChannel = memberVoiceState != null ? memberVoiceState.getChannel() : null;
        this.musicManager = playerManager.getGuildMusicManager(guild);
        this.audioPlayer = musicManager.player;
        this.scheduler = musicManager.scheduler;
        this.audioTrackInfo = audioPlayer.getPlayingTrack() != null ? audioPlayer.getPlayingTrack().getInfo() : null;
        this.volume = this.guildChannel.getVolume();
        this.queue = musicManager.scheduler.getQueue();
    }

    public void setBounded() {
        isBounded = true;
    }

    private void sendMessageAndDelete(MessageEmbed message) {
        channel.sendMessage(MessageCreateData.fromEmbeds(message)).queue(sentMessage -> {
            sentMessage.delete().queueAfter(DEFAULT_DELAY, TimeUnit.SECONDS);
        });
    }
}

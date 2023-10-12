package com.tzesh.tzebot.core;

import com.tzesh.tzebot.commands.moderation.Help;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.commands.moderation.*;
import com.tzesh.tzebot.commands.music.*;
import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.tzesh.tzebot.core.inventory.Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS;
import static com.tzesh.tzebot.core.inventory.Inventory.INITIALIZED_MUSIC_CHANNELS;

/**
 * A class to manage commands and their execution
 * @author tzesh
 * @see Command
 * @see CommandContextImpl
 * @see MessageReceivedEvent
 */
public class CommandManager {

    private final List<Command<MessageReceivedEvent>> commands = new ArrayList<>();
    private final long guildID;

    private final String prefix;

    public CommandManager(long guildID, String prefix) {
        this.guildID = guildID;
        this.prefix = prefix;
        initializeCommands();
    }
    private void initializeCommands() {
        addCommand(new Help(this));
        addCommand(new Language());
        addCommand(new Prefix());
        addCommand(new Clear());
        addCommand(new Ban());
        addCommand(new Vote());
        addCommand(new VoteRole());
        addCommand(new Play<>());
        addCommand(new Pause<>());
        addCommand(new Resume<>());
        addCommand(new Stop<>());
        addCommand(new Skip<>());
        addCommand(new Queue<>());
        addCommand(new Join<>());
        addCommand(new Leave<>());
        addCommand(new NowPlaying<>());
        addCommand(new Volume<>());
        addCommand(new Loop<>());
        addCommand(new Channel<>());
        addCommand(new Shuffle<>());
        addCommand(new Seek<>());
    }

    private void addCommand(Command<MessageReceivedEvent> cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName(guildID).equalsIgnoreCase(cmd.getName(guildID)));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present.");
        }

        commands.add(cmd);
    }

    @Nullable
    public Command<MessageReceivedEvent> getCommand(String search, long guildID) {
        String searchLower = search.toLowerCase();

        for (Command<MessageReceivedEvent> cmd : this.commands) {
            if (cmd.getName(guildID).equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    /**
     * A method to handle all commands
     * @param event the event to handle
     */
    public void handleCommand(MessageReceivedEvent event) {
        // split message into command and arguments
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        // get music channel id
        Long musicChannelID = INITIALIZED_MUSIC_CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> 0L);

        // control if this is a music channel
        boolean isMusicChannel = musicChannelID != 0L && isExists(event);

        // get command alias
        String commandAlias = split[0].toLowerCase();

        // get command
        Command<MessageReceivedEvent> command = this.getCommand(commandAlias, guildID);

        // check if command exists
        if (command == null && !isMusicChannel) return;
        else if (isMusicChannel) command = new Play<MessageReceivedEvent>();

        // control if this is a music command
        boolean isMusicCommand = command.getClass().getPackage().getName().contains("music");

        // control if bounded music channel is set
        boolean boundedMusicChannelSet = isMusicCommand && isMusicChannel;

        // if bounded music channel is set, set command as bounded
        if (boundedMusicChannelSet) {
            AbstractMusicCommand<MessageReceivedEvent> abstractMusicCommand = (AbstractMusicCommand<MessageReceivedEvent>) command;
            abstractMusicCommand.setBounded();
            split = event.getMessage().getContentRaw().split("\\s+");
        }

        // starting index of arguments list
        int startIndex = boundedMusicChannelSet ? 0 : 1;

        // get arguments list (music channel is 0 due to there is no prefix)
        List<String> args = List.of(split).subList(startIndex, split.length);

        // create command context
        CommandContextImpl<MessageReceivedEvent> ctx = new CommandContextImpl<>(event, args);

        // handle command
        command.handle(ctx);
    }

    /**
     * A method to check if the music channel exists
     * @param event the event to check
     * @return true if exists, false otherwise
     */
    public boolean isExists(MessageReceivedEvent event) {
        long guildId = event.getGuild().getIdLong();
        return EMOJI_CONTROLLED_MUSIC_CHANNELS.containsKey(guildId) && EMOJI_CONTROLLED_MUSIC_CHANNELS.get(guildId).containsKey(event.getChannel().getIdLong());
    }
}
package com.tzesh.tzebot.core.command;

import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.commands.moderation.*;
import com.tzesh.tzebot.commands.music.*;
import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class to manage commands and their execution
 * @author tzesh
 * @see Command
 * @see CommandContextImpl
 * @see MessageReceivedEvent
 */
public class CommandManager {

    private final List<Command<MessageReceivedEvent>> commands = new ArrayList<>();
    private final GuildChannel guildChannel;
    private final Long guildID;
    private final String prefix;

    public CommandManager(GuildChannel guildChannel) {
        this.guildChannel = guildChannel;
        this.guildID = guildChannel.getGuildID();
        this.prefix = guildChannel.getPrefix();
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
        // control every single command is unique by class name
        for (Command<MessageReceivedEvent> command : this.commands) {
            if (command.getClass().getName().equals(cmd.getClass().getName())) {
                throw new IllegalArgumentException("Command already exists");
            }
        }

        commands.add(cmd);
    }

    @Nullable
    public Command<MessageReceivedEvent> getCommand(String search) {
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

        // control if this is a music channel
        boolean isMusicChannel = isMusicChannel(event);

        // get command alias
        String commandAlias = split[0].toLowerCase();

        // get command
        Command<MessageReceivedEvent> command = this.getCommand(commandAlias);

        // check if command exists
        if (command == null && !isMusicChannel) return;
        else if (isMusicChannel) command = new Play<MessageReceivedEvent>();

        // control if this is a music command
        boolean isMusicCommand = command.getClass().getPackage().getName().contains("music");

        // control if bounded music channel is set
        boolean boundedMusicChannelCommand = isMusicCommand && isMusicChannel;

        // if bounded music channel is set, set command as bounded
        if (boundedMusicChannelCommand) {
            AbstractMusicCommand<MessageReceivedEvent> abstractMusicCommand = (AbstractMusicCommand<MessageReceivedEvent>) command;
            abstractMusicCommand.setBounded();
            split = event.getMessage().getContentRaw().split("\\s+");
        }

        // starting index of arguments list
        int startIndex = boundedMusicChannelCommand ? 0 : 1;

        // get arguments list (music channel is 0 due to there is no prefix)
        List<String> args = List.of(split).subList(startIndex, split.length);

        // create command context
        CommandContextImpl<MessageReceivedEvent> ctx = new CommandContextImpl<>(event, args, guildChannel);

        // handle command
        command.handle(ctx);
    }

    /**
     * A method to check if the music channel exists
     * @param event the event to check
     * @return true if exists, false otherwise
     */
    public boolean isMusicChannel(MessageReceivedEvent event) {
        return this.guildChannel.doesMusicChannelExist() && this.guildChannel.getMusicChannelMessageID() != null && this.guildChannel.getBoundedMusicChannelID().equals(event.getChannel().getIdLong());
    }
}
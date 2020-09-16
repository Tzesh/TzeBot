package TzeBot.essentials;

import TzeBot.commands.music.Pause;
import TzeBot.commands.music.Join;
import TzeBot.commands.music.Skip;
import TzeBot.commands.music.Loop;
import TzeBot.commands.music.Play;
import TzeBot.commands.music.Volume;
import TzeBot.commands.music.Stop;
import TzeBot.commands.music.Queue;
import TzeBot.commands.music.NowPlaying;
import TzeBot.commands.music.Leave;
import TzeBot.commands.music.Resume;
import TzeBot.commands.moderation.Prefix;
import TzeBot.commands.moderation.Clear;
import TzeBot.commands.Help;
import TzeBot.commands.Support;
import TzeBot.commands.moderation.Language;
import TzeBot.commands.moderation.Vote;
import TzeBot.commands.moderation.VoteRole;
import TzeBot.commands.music.Channel;
import TzeBot.commands.music.Shuffle;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.GuildChannel;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Help(this));
        addCommand(new Language());
        addCommand(new Prefix());
        addCommand(new Clear());
        addCommand(new Vote());
        addCommand(new Play());
        addCommand(new Pause());
        addCommand(new Resume());
        addCommand(new Stop());
        addCommand(new Skip());
        addCommand(new Queue());
        addCommand(new Join());
        addCommand(new Leave());
        addCommand(new NowPlaying());
        addCommand(new Volume());
        addCommand(new Loop());
        addCommand(new Support());
        addCommand(new VoteRole());
        addCommand(new Channel());
        addCommand(new Shuffle());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present.");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }


    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split =  event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        String searchLower = invoke.toLowerCase();
        Long textChannelID = Config.CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> 0L);
        if (textChannelID != 0L && isExists(event)) {
            for (ICommand cmd : this.commands) {
                if (cmd.getName().equals(searchLower)) {
                    if (cmd.getClass().getPackage().getName().equals("TzeBot.commands.music")) {
                        if (textChannelID == event.getChannel().getIdLong()) {
                            ICommand command = this.getCommand(invoke);
                            if (command != null) {
                                event.getChannel().sendTyping().queue();
                                List<String> args = List.of(split).subList(1, split.length);
                                CommandContext ctx = new CommandContext(event, args);
                                command.handle(ctx);
                            }
                        }
                    } else {
                        ICommand command = this.getCommand(invoke);
                        if (command != null) {
                            event.getChannel().sendTyping().queue();
                            List<String> args = List.of(split).subList(1, split.length);
                            CommandContext ctx = new CommandContext(event, args);
                            command.handle(ctx);
                    }
                }
                }
            }
        } else {
        ICommand command = this.getCommand(invoke);
        if (command != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = List.of(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(event, args);
            command.handle(ctx);
            }
        }
    }
    
    void handle(GuildMessageReceivedEvent event) {
        String[] split =  event.getMessage().getContentRaw()
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        String searchLower = invoke.toLowerCase();
        Long textChannelID = Config.CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> 0L);
        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (textChannelID != 0L && isExists(event) && IDs == null) {
            for (ICommand cmd : this.commands) {
                if (cmd.getName().equals(searchLower)) {
                    if (cmd.getClass().getPackage().getName().equals("TzeBot.commands.music")) {
                        if (textChannelID == event.getChannel().getIdLong()) {
                            ICommand command = this.getCommand(invoke);
                            if (command != null) {
                                event.getChannel().sendTyping().queue();
                                List<String> args = List.of(split).subList(1, split.length);
                                CommandContext ctx = new CommandContext(event, args);
                                command.handle(ctx);
                            }
                        }
                    } else {
                        ICommand command = this.getCommand(invoke);
                        if (command != null) {
                            event.getChannel().sendTyping().queue();
                            List<String> args = List.of(split).subList(1, split.length);
                            CommandContext ctx = new CommandContext(event, args);
                            command.handle(ctx);
                    }
                }
                }
            }
        } else {
        if (IDs != null && IDs.containsKey(event.getChannel().getIdLong())) {
            ICommand command2 = this.getCommand(TzeBot.essentials.LanguageDetector.getMessage("play.name"));
            event.getChannel().sendTyping().queue();
            List<String> args = List.of(split).subList(0, split.length);
            CommandContext ctx = new CommandContext(event, args);
            command2.handle(ctx);
            return;
        }
        ICommand command = this.getCommand(invoke);
        if (command != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = List.of(split).subList(1, split.length);
            CommandContext ctx = new CommandContext(event, args);
            command.handle(ctx);
            } else {
            ICommand command1 = this.getCommand(TzeBot.essentials.LanguageDetector.getMessage("play.name"));
            event.getChannel().sendTyping().queue();
            List<String> args = List.of(split).subList(0, split.length);
            CommandContext ctx = new CommandContext(event, args);
            command1.handle(ctx);
            }
        }
    }
    
    public boolean isExists(GuildMessageReceivedEvent event) {
        Iterator<GuildChannel> iterator = event.getGuild().getChannels().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getIdLong() == Config.CHANNELS.get(event.getGuild().getIdLong())) {
                return true;
            }
        }
        Config.CHANNELS.put(event.getGuild().getIdLong(), 0L);
        return false;
    }
}

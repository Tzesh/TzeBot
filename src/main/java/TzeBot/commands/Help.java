package TzeBot.commands;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.CommandManager;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.List;

import static TzeBot.essentials.LanguageManager.getMessage;

public class Help implements ICommand {

    private final CommandManager manager;

    public Help(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        final String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(getMessage("help.info.setTitle", guildID), "https://tzesh.github.io/TzeBot/");
            info.setDescription(getMessage("help.info.setDescription", guildID));
            info.addField(getMessage("help.info.setDescription1", guildID), "`" + prefix + getMessage("help.name", guildID) + " " + getMessage("moderation.name", guildID) + "`", true);
            info.addField(getMessage("help.info.setDescription2", guildID), "`" + prefix + getMessage("help.name", guildID) + " " + getMessage("music.name", guildID) + "`", true);
            info.addField(getMessage("help.info.setDescription3", guildID), "`" + prefix + getMessage("support.name", guildID) + "`", true);
            info.setImage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/embed_logo.png");
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());
            info.setFooter(getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (args.get(0).equals(getMessage("music.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(getMessage("help.music.info.setTitle", guildID));
            info.setDescription(getMessage("help.music.info.setDescription", guildID));
            info.addField(getMessage("music.icon", guildID) + "`" + prefix + getMessage("channel.name", guildID) + "`", getMessage("channel.gethelp", guildID), true);
            info.addField(getMessage("general.icon.join", guildID) + "`" + prefix + getMessage("join.name", guildID) + "`", getMessage("join.gethelp", guildID), true);
            info.addField(getMessage("general.icon.leave", guildID) + "`" + prefix + getMessage("leave.name", guildID) + "`", getMessage("leave.gethelp", guildID), true);
            info.addField(getMessage("general.icon.play", guildID) + "`" + prefix + getMessage("play.name", guildID) + "`", getMessage("play.gethelp1", guildID) + ". " + getMessage("play.gethelp2", guildID) + prefix + getMessage("play.name", guildID) + getMessage("play.gethelp3", guildID), true);
            info.addField(getMessage("general.icon.pause", guildID) + "`" + prefix + getMessage("pause.name", guildID) + "`", getMessage("pause.gethelp", guildID), true);
            info.addField(getMessage("general.icon.play", guildID) + "`" + prefix + getMessage("resume.name", guildID) + "`", getMessage("resume.gethelp", guildID), true);
            info.addField(getMessage("general.icon.stop", guildID) + "`" + prefix + getMessage("stop.name", guildID) + "`", getMessage("stop.gethelp", guildID), true);
            info.addField(getMessage("general.icon.skip", guildID) + "`" + prefix + getMessage("skip.name", guildID) + "`", getMessage("skip.gethelp", guildID), true);
            info.addField(getMessage("general.icon.volume", guildID) + "`" + prefix + getMessage("volume.name", guildID) + "`", getMessage("volume.gethelp", guildID), true);
            info.addField(getMessage("general.icon.loop", guildID) + "`" + prefix + getMessage("loop.name", guildID) + "`", getMessage("loop.gethelp", guildID), true);
            info.addField(getMessage("general.icon.nowplaying", guildID) + "`" + prefix + getMessage("nowplaying.name", guildID) + "`", getMessage("nowplaying.gethelp", guildID), true);
            info.addField(getMessage("general.icon.queue", guildID) + "`" + prefix + getMessage("queue.name", guildID) + "`", getMessage("queue.gethelp", guildID), true);
            info.addField(getMessage("general.icon.next", guildID) + "`" + prefix + getMessage("seek.name", guildID) + "`" + getMessage("general.icon.previous", guildID), getMessage("seek.gethelp", guildID), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());

            info.setFooter(getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());
            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (args.get(0).equals(getMessage("moderation.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(getMessage("help.moderation.info.setTitle", guildID));
            info.setDescription(getMessage("help.moderation.info.setDescription", guildID));
            info.addField(getMessage("general.icon.prefix", guildID) + "`" + prefix + getMessage("prefix.name", guildID) + "`", getMessage("prefix.gethelp1", guildID) + " " + getMessage("prefix.gethelp2", guildID) + prefix + getMessage("prefix.gethelp3", guildID), true);
            info.addField(getMessage("general.icon.clear", guildID) + "`" + prefix + getMessage("clear.name", guildID) + "`", getMessage("clear.gethelp", guildID), true);
            info.addField(getMessage("general.icon.ban", guildID) + "`" + prefix + getMessage("ban.name", guildID) + "`", getMessage("ban.description", guildID), true);
            info.addField(getMessage("general.icon.language", guildID) + "`" + prefix + getMessage("language.name", guildID) + "`", getMessage("language.gethelp", guildID), true);
            info.addField(getMessage("general.icon.vote", guildID) + "`" + prefix + getMessage("vote.name", guildID) + "`", getMessage("vote.gethelp", guildID), true);
            info.addField(getMessage("general.icon.vote", guildID) + "`" + prefix + getMessage("voterole.name", guildID) + "`", getMessage("voterole.gethelp", guildID), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());
            info.setFooter(getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search, guildID);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.404", guildID));
            error.setDescription(getMessage("general.404.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(0xffffff);
            help.setTitle(command.getName(guildID) + getMessage("general.icon.question"));
            help.setDescription(command.getHelp(guildID));
            help.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            help.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(help.build())).queue();
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("help.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("help.gethelp1", guildID) + "\n"
                + getMessage("help.gethelp2", guildID) + Config.get("pre") + getMessage("help.gethelp3", guildID);
    }
}

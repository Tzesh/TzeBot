package TzeBot.commands;

import TzeBot.essentials.CommandManager;
import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

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
            info.setTitle(LanguageDetector.getMessage("help.info.setTitle", guildID));
            info.setDescription(LanguageDetector.getMessage("help.info.setDescription", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.link", guildID) + "https://tzesh.github.io/TzeBot/"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription1", guildID) + "`" + prefix + LanguageDetector.getMessage("help.name", guildID) + " " + LanguageDetector.getMessage("moderation.name", guildID) + "`"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription2", guildID) + "`" + prefix + LanguageDetector.getMessage("help.name", guildID) + " " + LanguageDetector.getMessage("music.name", guildID) + "`"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription3", guildID) + "`" + prefix + LanguageDetector.getMessage("support.name", guildID) + "`");
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(LanguageDetector.getMessage("music.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageDetector.getMessage("help.music.info.setTitle", guildID));
            info.setDescription(LanguageDetector.getMessage("help.music.info.setDescription", guildID)
                    + "\n" + LanguageDetector.getMessage("music.icon", guildID) + "`" + prefix + LanguageDetector.getMessage("channel.name", guildID) + "`" + ": " + LanguageDetector.getMessage("channel.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.join", guildID) + "`" + prefix + LanguageDetector.getMessage("join.name", guildID) + "`" + ": " + LanguageDetector.getMessage("join.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.leave", guildID) + "`" + prefix + LanguageDetector.getMessage("leave.name", guildID) + "`" + ": " + LanguageDetector.getMessage("leave.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.play", guildID) + "`" + prefix + LanguageDetector.getMessage("play.name", guildID) + "`" + ": " + LanguageDetector.getMessage("play.gethelp1", guildID) + ". " + LanguageDetector.getMessage("play.gethelp2", guildID) + prefix + LanguageDetector.getMessage("play.name", guildID) + LanguageDetector.getMessage("play.gethelp3", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.pause", guildID) + "`" + prefix + LanguageDetector.getMessage("pause.name", guildID) + "`" + ": " + LanguageDetector.getMessage("pause.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.play", guildID) + "`" + prefix + LanguageDetector.getMessage("resume.name", guildID) + "`" + ": " + LanguageDetector.getMessage("resume.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.stop", guildID) + "`" + prefix + LanguageDetector.getMessage("stop.name", guildID) + "`" + ": " + LanguageDetector.getMessage("stop.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.skip", guildID) + "`" + prefix + LanguageDetector.getMessage("skip.name", guildID) + "`" + ": " + LanguageDetector.getMessage("skip.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.volume", guildID) + "`" + prefix + LanguageDetector.getMessage("volume.name", guildID) + "`" + ": " + LanguageDetector.getMessage("volume.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.loop", guildID) + "`" + prefix + LanguageDetector.getMessage("loop.name", guildID) + "`" + ": " + LanguageDetector.getMessage("loop.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.nowplaying", guildID) + "`" + prefix + LanguageDetector.getMessage("nowplaying.name", guildID) + "`" + ": " + LanguageDetector.getMessage("nowplaying.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.queue", guildID) + "`" + prefix + LanguageDetector.getMessage("queue.name", guildID) + "`" + ": " + LanguageDetector.getMessage("queue.gethelp", guildID));
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(LanguageDetector.getMessage("moderation.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageDetector.getMessage("help.moderation.info.setTitle", guildID));
            info.setDescription(LanguageDetector.getMessage("help.moderation.info.setDescription", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.prefix", guildID) + "`" + prefix + LanguageDetector.getMessage("prefix.name", guildID) + "`" + ": " + LanguageDetector.getMessage("prefix.gethelp1", guildID) + " " + LanguageDetector.getMessage("prefix.gethelp2", guildID) + prefix + LanguageDetector.getMessage("prefix.gethelp3", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.clear", guildID) + "`" + prefix + LanguageDetector.getMessage("clear.name", guildID) + "`" + ": " + LanguageDetector.getMessage("clear.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.language", guildID) + "`" + prefix + LanguageDetector.getMessage("language.name", guildID) + "`" + ": " + LanguageDetector.getMessage("language.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.vote", guildID) + "`" + prefix + LanguageDetector.getMessage("vote.name", guildID) + "`" + ": " + LanguageDetector.getMessage("vote.gethelp", guildID)
                    + "\n" + LanguageDetector.getMessage("general.icon.vote", guildID) + "`" + prefix + LanguageDetector.getMessage("voterole.name", guildID) + "`" + ": " + LanguageDetector.getMessage("voterole.gethelp", guildID));
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search, guildID);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.404", guildID));
            error.setDescription(LanguageDetector.getMessage("general.404.description", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        channel.sendMessage(command.getHelp(guildID)).queue();
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("help.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("help.gethelp1", guildID) + "\n"
                + LanguageDetector.getMessage("help.gethelp2", guildID) + Config.get("pre") + LanguageDetector.getMessage("help.gethelp3", guildID);
    }
}

package TzeBot.commands;

import TzeBot.essentials.CommandManager;
import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

import static TzeBot.essentials.LanguageDetector.getMessage;

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
            info.setTitle(getMessage("help.info.setTitle", guildID));
            info.setDescription(getMessage("help.info.setDescription", guildID)
                    + "\n" + getMessage("general.icon.link", guildID) + "https://tzesh.github.io/TzeBot/"
                    + "\n" + getMessage("help.info.setDescription1", guildID) + "`" + prefix + getMessage("help.name", guildID) + " " + getMessage("moderation.name", guildID) + "`"
                    + "\n" + getMessage("help.info.setDescription2", guildID) + "`" + prefix + getMessage("help.name", guildID) + " " + getMessage("music.name", guildID) + "`"
                    + "\n" + getMessage("help.info.setDescription3", guildID) + "`" + prefix + getMessage("support.name", guildID) + "`");
            info.setColor(0x6699ff);
            info.setFooter(getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(getMessage("music.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(getMessage("help.music.info.setTitle", guildID));
            info.setDescription(getMessage("help.music.info.setDescription", guildID)
                    + "\n" + getMessage("music.icon", guildID) + "`" + prefix + getMessage("channel.name", guildID) + "`" + ": " + getMessage("channel.gethelp", guildID)
                    + "\n" + getMessage("general.icon.join", guildID) + "`" + prefix + getMessage("join.name", guildID) + "`" + ": " + getMessage("join.gethelp", guildID)
                    + "\n" + getMessage("general.icon.leave", guildID) + "`" + prefix + getMessage("leave.name", guildID) + "`" + ": " + getMessage("leave.gethelp", guildID)
                    + "\n" + getMessage("general.icon.play", guildID) + "`" + prefix + getMessage("play.name", guildID) + "`" + ": " + getMessage("play.gethelp1", guildID) + ". " + getMessage("play.gethelp2", guildID) + prefix + getMessage("play.name", guildID) + getMessage("play.gethelp3", guildID)
                    + "\n" + getMessage("general.icon.pause", guildID) + "`" + prefix + getMessage("pause.name", guildID) + "`" + ": " + getMessage("pause.gethelp", guildID)
                    + "\n" + getMessage("general.icon.play", guildID) + "`" + prefix + getMessage("resume.name", guildID) + "`" + ": " + getMessage("resume.gethelp", guildID)
                    + "\n" + getMessage("general.icon.stop", guildID) + "`" + prefix + getMessage("stop.name", guildID) + "`" + ": " + getMessage("stop.gethelp", guildID)
                    + "\n" + getMessage("general.icon.skip", guildID) + "`" + prefix + getMessage("skip.name", guildID) + "`" + ": " + getMessage("skip.gethelp", guildID)
                    + "\n" + getMessage("general.icon.volume", guildID) + "`" + prefix + getMessage("volume.name", guildID) + "`" + ": " + getMessage("volume.gethelp", guildID)
                    + "\n" + getMessage("general.icon.loop", guildID) + "`" + prefix + getMessage("loop.name", guildID) + "`" + ": " + getMessage("loop.gethelp", guildID)
                    + "\n" + getMessage("general.icon.nowplaying", guildID) + "`" + prefix + getMessage("nowplaying.name", guildID) + "`" + ": " + getMessage("nowplaying.gethelp", guildID)
                    + "\n" + getMessage("general.icon.queue", guildID) + "`" + prefix + getMessage("queue.name", guildID) + "`" + ": " + getMessage("queue.gethelp", guildID));
            info.setColor(0x6699ff);
            info.setFooter(getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(getMessage("moderation.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(getMessage("help.moderation.info.setTitle", guildID));
            info.setDescription(getMessage("help.moderation.info.setDescription", guildID)
                    + "\n" + getMessage("general.icon.prefix", guildID) + "`" + prefix + getMessage("prefix.name", guildID) + "`" + ": " + getMessage("prefix.gethelp1", guildID) + " " + getMessage("prefix.gethelp2", guildID) + prefix + getMessage("prefix.gethelp3", guildID)
                    + "\n" + getMessage("general.icon.clear", guildID) + "`" + prefix + getMessage("clear.name", guildID) + "`" + ": " + getMessage("clear.gethelp", guildID)
                    + "\n" + getMessage("general.icon.language", guildID) + "`" + prefix + getMessage("language.name", guildID) + "`" + ": " + getMessage("language.gethelp", guildID)
                    + "\n" + getMessage("general.icon.vote", guildID) + "`" + prefix + getMessage("vote.name", guildID) + "`" + ": " + getMessage("vote.gethelp", guildID)
                    + "\n" + getMessage("general.icon.vote", guildID) + "`" + prefix + getMessage("voterole.name", guildID) + "`" + ": " + getMessage("voterole.gethelp", guildID));
            info.setColor(0x6699ff);
            info.setFooter(getMessage("help.info.setFooter", guildID), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search, guildID);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.404", guildID));
            error.setDescription(getMessage("general.404.description", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        channel.sendMessage(command.getHelp(guildID)).queue();
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

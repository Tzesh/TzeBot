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

        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageDetector.getMessage("help.info.setTitle"));
            info.setDescription(LanguageDetector.getMessage("help.info.setDescription")
                    + "\n" + LanguageDetector.getMessage("general.icon.link") + "https://tzesh.github.io/TzeBot/"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription1") + "`" + prefix + LanguageDetector.getMessage("help.name") + " " + LanguageDetector.getMessage("moderation.name") + "`"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription2") + "`" + prefix + LanguageDetector.getMessage("help.name") + " " + LanguageDetector.getMessage("music.name") + "`"
                    + "\n" + LanguageDetector.getMessage("help.info.setDescription3") + "`" + prefix + LanguageDetector.getMessage("support.name") + "`");
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(LanguageDetector.getMessage("music.name"))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageDetector.getMessage("help.music.info.setTitle"));
            info.setDescription(LanguageDetector.getMessage("help.music.info.setDescription")
                    + "\n" + LanguageDetector.getMessage("music.icon") + "`" + prefix + LanguageDetector.getMessage("channel.name") + "`" + ": " + LanguageDetector.getMessage("channel.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.join") + "`" + prefix + LanguageDetector.getMessage("join.name") + "`" + ": " + LanguageDetector.getMessage("join.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.leave") + "`" + prefix + LanguageDetector.getMessage("leave.name") + "`" + ": " + LanguageDetector.getMessage("leave.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.play") + "`" + prefix + LanguageDetector.getMessage("play.name") + "`" + ": " + LanguageDetector.getMessage("play.gethelp1") + ". " + LanguageDetector.getMessage("play.gethelp2") + prefix + LanguageDetector.getMessage("play.name") + LanguageDetector.getMessage("play.gethelp3")
                    + "\n" + LanguageDetector.getMessage("general.icon.pause") + "`" + prefix + LanguageDetector.getMessage("pause.name") + "`" + ": " + LanguageDetector.getMessage("pause.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.play") + "`" + prefix + LanguageDetector.getMessage("resume.name") + "`" + ": " + LanguageDetector.getMessage("resume.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.stop") + "`" + prefix + LanguageDetector.getMessage("stop.name") + "`" + ": " + LanguageDetector.getMessage("stop.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.skip") + "`" + prefix + LanguageDetector.getMessage("skip.name") + "`" + ": " + LanguageDetector.getMessage("skip.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.volume") + "`" + prefix + LanguageDetector.getMessage("volume.name") + "`" + ": " + LanguageDetector.getMessage("volume.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.loop") + "`" + prefix + LanguageDetector.getMessage("loop.name") + "`" + ": " + LanguageDetector.getMessage("loop.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.nowplaying") + "`" + prefix + LanguageDetector.getMessage("nowplaying.name") + "`" + ": " + LanguageDetector.getMessage("nowplaying.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.queue") + "`" + prefix + LanguageDetector.getMessage("queue.name") + "`" + ": " + LanguageDetector.getMessage("queue.gethelp"));
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        if (args.get(0).equals(LanguageDetector.getMessage("moderation.name"))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageDetector.getMessage("help.moderation.info.setTitle"));
            info.setDescription(LanguageDetector.getMessage("help.moderation.info.setDescription")
                    + "\n" + LanguageDetector.getMessage("general.icon.prefix") + "`" + prefix + LanguageDetector.getMessage("prefix.name") + "`" + ": " + LanguageDetector.getMessage("prefix.gethelp1") + " " + LanguageDetector.getMessage("prefix.gethelp2") + prefix + LanguageDetector.getMessage("prefix.gethelp3")
                    + "\n" + LanguageDetector.getMessage("general.icon.clear") + "`" + prefix + LanguageDetector.getMessage("clear.name") + "`" + ": " + LanguageDetector.getMessage("clear.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.language") + "`" + prefix + LanguageDetector.getMessage("language.name") + "`" + ": " + LanguageDetector.getMessage("language.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.vote") + "`" + prefix + LanguageDetector.getMessage("vote.name") + "`" + ": " + LanguageDetector.getMessage("vote.gethelp")
                    + "\n" + LanguageDetector.getMessage("general.icon.vote") + "`" + prefix + LanguageDetector.getMessage("voterole.name") + "`" + ": " + LanguageDetector.getMessage("voterole.gethelp"));
            info.setColor(0x6699ff);
            info.setFooter(LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("general.404"));
            error.setDescription(LanguageDetector.getMessage("general.404.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return LanguageDetector.getMessage("help.name");
    }

    @Override
    public String getHelp() {
        return LanguageDetector.getMessage("help.gethelp1") + "\n"
                + LanguageDetector.getMessage("help.gethelp2") + Config.get("pre") + LanguageDetector.getMessage("help.gethelp3");
    }
}

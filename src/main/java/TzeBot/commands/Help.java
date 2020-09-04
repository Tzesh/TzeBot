package TzeBot.commands;

import TzeBot.essentials.CommandManager;
import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
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
        String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        
        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(TzeBot.essentials.LanguageDetector.getMessage("help.info.setTitle"));
            info.setDescription(TzeBot.essentials.LanguageDetector.getMessage("help.info.setDescription") + 
                        "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.link") + "https://tzesh.github.io/TzeBot/" +
                        "\n" + TzeBot.essentials.LanguageDetector.getMessage("help.info.setDescription1") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("help.name") + " " + TzeBot.essentials.LanguageDetector.getMessage("moderation.name") + "`" +
                        "\n" + TzeBot.essentials.LanguageDetector.getMessage("help.info.setDescription2") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("help.name") + " " + TzeBot.essentials.LanguageDetector.getMessage("music.name") + "`" +
                        "\n" + TzeBot.essentials.LanguageDetector.getMessage("help.info.setDescription3") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("support.name") + "`");
            info.setColor(0x6699ff);
            info.setFooter(TzeBot.essentials.LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }
        
        if (args.get(0).equals(TzeBot.essentials.LanguageDetector.getMessage("music.name"))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(TzeBot.essentials.LanguageDetector.getMessage("help.music.info.setTitle"));
            info.setDescription(TzeBot.essentials.LanguageDetector.getMessage("help.music.info.setDescription")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.join") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("join.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("join.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.leave") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("leave.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("leave.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.play") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("play.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("play.gethelp1") + ". " + TzeBot.essentials.LanguageDetector.getMessage("play.gethelp2") + prefix + TzeBot.essentials.LanguageDetector.getMessage("play.name") + TzeBot.essentials.LanguageDetector.getMessage("play.gethelp3")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.pause") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("pause.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("pause.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.stop") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("stop.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("stop.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.skip")  + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("skip.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("skip.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.volume") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("volume.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("volume.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.loop")  + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("loop.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("loop.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.nowplaying") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("nowplaying.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("nowplaying.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.shuffle")  + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("shuffle.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("shuffle.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.queue")  + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("queue.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("queue.gethelp"));
            info.setColor(0x6699ff);
            info.setFooter(TzeBot.essentials.LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            info.clear();
            return;
        }
        
        if (args.get(0).equals(TzeBot.essentials.LanguageDetector.getMessage("moderation.name"))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(TzeBot.essentials.LanguageDetector.getMessage("help.moderation.info.setTitle"));
            info.setDescription(TzeBot.essentials.LanguageDetector.getMessage("help.moderation.info.setDescription")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.prefix") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("prefix.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp1") + " " + TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp2") + prefix + TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp3")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.clear") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("clear.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("clear.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.language") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("language.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("language.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + "`" + prefix + TzeBot.essentials.LanguageDetector.getMessage("vote.name") + "`" + ": " + TzeBot.essentials.LanguageDetector.getMessage("vote.gethelp"));
            info.setColor(0x6699ff);
            info.setFooter(TzeBot.essentials.LanguageDetector.getMessage("help.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

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
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.404"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.404.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("help.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("help.gethelp1") + "\n" + 
                TzeBot.essentials.LanguageDetector.getMessage("help.gethelp2") + Config.get("pre") + TzeBot.essentials.LanguageDetector.getMessage("help.gethelp3");
    }
}

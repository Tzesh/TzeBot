package TzeBot.command.commands;

import TzeBot.CommandManager;
import TzeBot.Config;
import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;


    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());

            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(TzeBot.Languages.LanguageDetector.getMessage("helpcommand.info.setTitle"));
            info.setDescription(TzeBot.Languages.LanguageDetector.getMessage("helpcommand.info.setDescription"));
            info.setColor(0x6699ff);
            info.setFooter(TzeBot.Languages.LanguageDetector.getMessage("helpcommand.info.setFooter"), "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            builder.append(TzeBot.Languages.LanguageDetector.getMessage("helpcommand.builder.append") + "\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append('`')
                            .append(prefix)
                            .append(it)
                            .append("`\n")
            );

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            channel.sendMessage(builder.toString()).queue();
            channel.sendMessage(TzeBot.Languages.LanguageDetector.getMessage("helpcommand.further1") + prefix + TzeBot.Languages.LanguageDetector.getMessage("helpcommand.further2")).queue();
            info.clear();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("general.404"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("general.404.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();

    }

    @Override
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("helpcommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("helpcommand.gethelp1") + "\n" + 
                TzeBot.Languages.LanguageDetector.getMessage("helpcommand.gethelp2") + Config.get("pre") + TzeBot.Languages.LanguageDetector.getMessage("helpcommand.gethelp3");
    }
}

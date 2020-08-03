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
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());

            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("TzeBOT BOT BOT BOT BOT BOT...");
            info.setDescription("Just a normal type bot which born in Java.");
            info.setColor(0x6699ff);
            info.setFooter("Frankensteined by Tzesh." + "https://cdn.discordapp.com/avatars/217668226845245440/8326b24c3961afa0b3727a771220719e.png");

            builder.append("List of commands:\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append('`')
                            .append(prefix)
                            .append(it)
                            .append("`\n")
            );

            channel.sendTyping().queue();
            channel.sendMessage(info.build()).queue();
            channel.sendMessage(builder.toString()).queue();
            channel.sendMessage("You may also use `" + prefix + "help [command]` to get further information.").queue();
            info.clear();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Nothing found.");
            error.setDescription("Are you sure looking for the right thing?");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();

    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list with commands in the bot\n" +
                "Usage: `" + Config.get("pre") + "help [command]`";
    }
}

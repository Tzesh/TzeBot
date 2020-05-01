package TzeBot.command;

import TzeBot.Config;
import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ClearCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Unauthorized person.");
            error.setDescription("You must have MANAGE_SERVER permission to use this command.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        else if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ None args specified.");
            error.setDescription("Please enter the number of messages that you want to delete.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        else {
            try {
                List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(String.join(" ", args))).complete();
                channel.deleteMessages(messages).queue();

                EmbedBuilder succces = new EmbedBuilder();
                succces.setColor(0x00ff00);
                succces.setTitle("✅ " + String.join("", args) + " message has been cleared.");

                ctx.getChannel().sendTyping().queue();
                channel.sendMessage(succces.build()).queue();
                succces.clear();
            } catch (IllegalArgumentException exception) {
                if (exception.toString().startsWith("java.lang.IllegalArgumentException")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle("❌ Unsuitable amount of messages.");
                    error.setDescription("You may only choose 2-100 messages at once.");

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle("❌ Selected messages are older than 2 weeks.");
                    error.setDescription("Messages older than 2 weeks cannot be deleted.");

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            }
        }
        }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Deletes the specified amount of messages.";
    }
}

package TzeBot.command.commands.music;

import TzeBot.Config;
import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SetPrefixCommand implements ICommand {

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

        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ None args specified.");
            error.setDescription("Please specify an instance of prefix that you want to use for it.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        final String newPrefix = String.join("", args);
        Config.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle("✅ New prefix has been set to `" + newPrefix + "`.");
        succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server\n" +
                "Usage: `" + Config.get("pre") + "prefix <prefix>`";
    }
}

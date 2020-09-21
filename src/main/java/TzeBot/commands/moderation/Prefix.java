package TzeBot.commands.moderation;

import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Prefix implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageDetector.getMessage("general.not_authorized.description", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.403", guildID));
            error.setDescription(LanguageDetector.getMessage("general.403.description", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        final String newPrefix = String.join("", args);
        Config.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(LanguageDetector.getMessage("general.icon.success", guildID) + " " + LanguageDetector.getMessage("prefix.success.setTitle", guildID) + "`" + newPrefix + "`");
        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("prefix.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("prefix.gethelp1", guildID)
                + "\n" + LanguageDetector.getMessage("prefix.gethelp2", guildID) + Config.get("pre") + LanguageDetector.getMessage("prefix.gethelp3", guildID);
    }
}

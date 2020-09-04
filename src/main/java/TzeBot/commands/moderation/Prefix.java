package TzeBot.commands.moderation;

import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
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

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.not_authorized"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.not_authorized.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        final String newPrefix = String.join("", args);
        Config.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.success") + " " + TzeBot.essentials.LanguageDetector.getMessage("prefix.success.setTitle") + "`" + newPrefix + "`");
        success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("prefix.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp1") +
                "\n" + TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp2") + Config.get("pre") + TzeBot.essentials.LanguageDetector.getMessage("prefix.gethelp3");
    }
}

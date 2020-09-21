/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.commands.moderation;

import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import java.util.List;
import java.util.Locale;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author Tzesh
 */
public class Language implements ICommand {

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
        } else {
            if (Config.LANGUAGES.get(ctx.getGuild().getIdLong()).equals("en_en")) {
                if (args.get(0).toLowerCase().equals("turkish")) {
                    Config.LANGUAGES.put(ctx.getGuild().getIdLong(), "tr_tr");
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.success", guildID) + LanguageDetector.getMessage("language.successful.setTitle", guildID));
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                    ctx.getChannel().sendTyping().queue();
                    channel.sendMessage(success.build()).queue();
                    success.clear();
                    return;
                }
                if (args.get(0).toLowerCase().equals("english")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("language.already.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("language.already.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("language.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("language.unsuitable.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                }
            }
            if (Config.LANGUAGES.get(ctx.getGuild().getIdLong()).equals("tr_tr")) {
                if (args.get(0).toLowerCase(new Locale("tr", "TR")).equals("ingilizce")) {
                    Config.LANGUAGES.put(ctx.getGuild().getIdLong(), "en_en");
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.success", guildID) + LanguageDetector.getMessage("language.successful.setTitle", guildID));
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                    ctx.getChannel().sendTyping().queue();
                    channel.sendMessage(success.build()).queue();
                    success.clear();
                    return;
                }
                if (LanguageDetector.normalizer(args.get(0)).toLowerCase().equals("turkce")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("language.already.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("language.already.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("language.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("language.unsuitable.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("language.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("language.gethelp", guildID);
    }

}

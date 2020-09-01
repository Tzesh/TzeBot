/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.command.commands;

import TzeBot.Languages.LanguageDetector;
import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
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
public class LanguageCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        
            if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.not_authorized"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("general.not_authorized.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            }
            if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            } else {
                if (TzeBot.Languages.LanguageDetector.shortening.equals("en_en")) {
                    if (args.get(0).toLowerCase().equals("turkish")) {
                        TzeBot.Languages.LanguageDetector.selectLanguage("tr_tr");
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.successful.setTitle"));
                        success.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                        ctx.getChannel().sendTyping().queue();
                        channel.sendMessage(success.build()).queue();
                        success.clear();
                        return;
                    } if (args.get(0).toLowerCase().equals("english")) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.already.setTitle"));
                        error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("languagecommand.already.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    } else {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.unsuitable.setTitle"));
                        error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("languagecommand.unsuitable.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    } 
                }
                if (TzeBot.Languages.LanguageDetector.shortening.equals("tr_tr")) {
                       if (args.get(0).toLowerCase(new Locale("tr","TR")).equals("ingilizce")) {
                        TzeBot.Languages.LanguageDetector.selectLanguage("en_en");
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.successful.setTitle"));
                        success.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                        ctx.getChannel().sendTyping().queue();
                        channel.sendMessage(success.build()).queue();
                        success.clear();
                        return; 
                    } if (LanguageDetector.normalizer(args.get(0)).toLowerCase().equals("turkce")) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.already.setTitle"));
                        error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("languagecommand.already.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    } else {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("languagecommand.unsuitable.setTitle"));
                        error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("languagecommand.unsuitable.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    } 
            }
        }
    }

    @Override
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("languagecommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("languagecommand.gethelp");
    }
    
}

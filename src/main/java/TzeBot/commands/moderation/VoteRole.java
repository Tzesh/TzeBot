/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import java.util.LinkedList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author Tzesh
 */
public class VoteRole implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        LinkedList<Long> roleIDs = new LinkedList<>();
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("general.not_authorized"));
            error.setDescription(LanguageDetector.getMessage("general.not_authorized.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (args.isEmpty() || ctx.getMessage().getMentionedRoles().size() < 2) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("general.403"));
            error.setDescription(LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            String space = " ";
            String res = String.join(space, args);
            String[] variables = res.split(" : ");
            for (int i = 0; i < ctx.getMessage().getMentionedRoles().size(); i++) {
                roleIDs.add(i, ctx.getMessage().getMentionedRoles().get(i).getIdLong());
            }
            switch (variables.length) {
                case 5:
                    if (roleIDs.size() != 2) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("vote.error.setTitle"));
                        error.setDescription(LanguageDetector.getMessage("vote.error.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    }

                    EmbedBuilder vote3 = new EmbedBuilder();
                    vote3.setColor(0x0087ff);
                    vote3.setTitle(LanguageDetector.getMessage("general.icon.vote") + variables[0] + LanguageDetector.getMessage("general.icon.question"));
                    vote3.setDescription(LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + LanguageDetector.getMessage("general.icon.2") + " " + variables[3]);
                    vote3.setFooter(LanguageDetector.getMessage("vote.setFooter"));
                    channel.sendTyping().queue();
                    channel.sendMessage(vote3.build()).queue(message -> {
                        message.addReaction(LanguageDetector.getMessage("general.icon.1")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.2")).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    vote3.clear();
                    break;
                case 7:
                    if (roleIDs.size() != 3) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("vote.error.setTitle"));
                        error.setDescription(LanguageDetector.getMessage("vote.error.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    }
                    EmbedBuilder vote4 = new EmbedBuilder();
                    vote4.setColor(0x0087ff);
                    vote4.setTitle(LanguageDetector.getMessage("general.icon.vote") + variables[0] + LanguageDetector.getMessage("general.icon.question"));
                    vote4.setDescription(LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + LanguageDetector.getMessage("general.icon.3") + " " + variables[3]);
                    vote4.setFooter(LanguageDetector.getMessage("vote.setFooter"));
                    channel.sendTyping().queue();
                    channel.sendMessage(vote4.build()).queue(message -> {
                        message.addReaction(LanguageDetector.getMessage("general.icon.1")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.2")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.3")).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    vote4.clear();
                    break;
                case 9:
                    if (roleIDs.size() != 4) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("vote.error.setTitle"));
                        error.setDescription(LanguageDetector.getMessage("vote.error.setDescription"));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue();
                        error.clear();
                        return;
                    }
                    EmbedBuilder vote5 = new EmbedBuilder();
                    vote5.setColor(0x0087ff);
                    vote5.setTitle(LanguageDetector.getMessage("general.icon.vote") + variables[0] + LanguageDetector.getMessage("general.icon.question"));
                    vote5.setDescription(LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + LanguageDetector.getMessage("general.icon.4") + " " + variables[4]);
                    vote5.setFooter(LanguageDetector.getMessage("vote.setFooter"));
                    channel.sendTyping().queue();
                    channel.sendMessage(vote5.build()).queue(message -> {
                        message.addReaction(LanguageDetector.getMessage("general.icon.1")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.2")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.3")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.4")).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    vote5.clear();
                    break;
                default:
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("vote.error.setTitle"));
                    error.setDescription(LanguageDetector.getMessage("vote.error.setDescription"));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return LanguageDetector.getMessage("voterole.name");
    }

    @Override
    public String getHelp() {
        return LanguageDetector.getMessage("voterole.gethelp");
    }
}

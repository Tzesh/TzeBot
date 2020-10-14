/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageManager;

import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author Tzesh
 */
public class Vote implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            
            return;
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
            error.setDescription(LanguageManager.getMessage("general.403.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            
        } else {
            String space = " ";
            String res = String.join(space, args);
            String[] variables = res.split(" : ");
            switch (variables.length) {
                case 3:
                    EmbedBuilder vote3 = new EmbedBuilder();
                    vote3.setColor(0x0087ff);
                    vote3.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote3.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]);
                    vote3.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));
                    
                    channel.sendMessage(vote3.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                    });
                    break;
                case 4:
                    EmbedBuilder vote4 = new EmbedBuilder();
                    vote4.setColor(0x0087ff);
                    vote4.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote4.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]);
                    vote4.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote4.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                    });
                    break;
                case 5:
                    EmbedBuilder vote5 = new EmbedBuilder();
                    vote5.setColor(0x0087ff);
                    vote5.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote5.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]);
                    vote5.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote5.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                    });
                    break;
                case 6:
                    EmbedBuilder vote6 = new EmbedBuilder();
                    vote6.setColor(0x0087ff);
                    vote6.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote6.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]
                            + "\n" + LanguageManager.getMessage("general.icon.5", guildID) + " " + variables[5]);
                    vote6.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote6.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.5", guildID)).queue();
                    });
                    break;
                case 7:
                    EmbedBuilder vote7 = new EmbedBuilder();
                    vote7.setColor(0x0087ff);
                    vote7.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote7.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]
                            + "\n" + LanguageManager.getMessage("general.icon.5", guildID) + " " + variables[5]
                            + "\n" + LanguageManager.getMessage("general.icon.6", guildID) + " " + variables[6]);
                    vote7.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote7.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.5", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.6", guildID)).queue();
                    });
                    break;
                case 8:
                    EmbedBuilder vote8 = new EmbedBuilder();
                    vote8.setColor(0x0087ff);
                    vote8.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote8.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]
                            + "\n" + LanguageManager.getMessage("general.icon.5", guildID) + " " + variables[5]
                            + "\n" + LanguageManager.getMessage("general.icon.6", guildID) + " " + variables[6]
                            + "\n" + LanguageManager.getMessage("general.icon.7", guildID) + " " + variables[7]);
                    vote8.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote8.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.5", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.6", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.7", guildID)).queue();
                    });
                    break;
                case 9:
                    EmbedBuilder vote9 = new EmbedBuilder();
                    vote9.setColor(0x0087ff);
                    vote9.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote9.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]
                            + "\n" + LanguageManager.getMessage("general.icon.5", guildID) + " " + variables[5]
                            + "\n" + LanguageManager.getMessage("general.icon.6", guildID) + " " + variables[6]
                            + "\n" + LanguageManager.getMessage("general.icon.7", guildID) + " " + variables[7]
                            + "\n" + LanguageManager.getMessage("general.icon.8", guildID) + " " + variables[8]);
                    vote9.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote9.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.5", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.6", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.7", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.8", guildID)).queue();
                    });
                    break;
                case 10:
                    EmbedBuilder vote10 = new EmbedBuilder();
                    vote10.setColor(0x0087ff);
                    vote10.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote10.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]
                            + "\n" + LanguageManager.getMessage("general.icon.5", guildID) + " " + variables[5]
                            + "\n" + LanguageManager.getMessage("general.icon.6", guildID) + " " + variables[6]
                            + "\n" + LanguageManager.getMessage("general.icon.7", guildID) + " " + variables[7]
                            + "\n" + LanguageManager.getMessage("general.icon.8", guildID) + " " + variables[8]
                            + "\n" + LanguageManager.getMessage("general.icon.9", guildID) + " " + variables[9]);
                    vote10.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote10.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.5", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.6", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.7", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.8", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.9", guildID)).queue();
                    });
                default:
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("vote.error.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("vote.error.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                    break;
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("vote.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("vote.gethelp", guildID);
    }

}

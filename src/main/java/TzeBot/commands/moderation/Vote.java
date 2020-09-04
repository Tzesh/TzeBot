/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
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
        }
        
        else {
	String space = " ";
	String res = String.join(space, args);
        String[] variables = res.split(" : ");
        switch(variables.length) {
            case 3:
            EmbedBuilder vote3 = new EmbedBuilder();
            vote3.setColor(0x0087ff);
            vote3.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote3.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]);
            vote3.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote3.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            });
            channel.sendMessage(res).queue();
            vote3.clear();
            break;
            case 4:
            EmbedBuilder vote4 = new EmbedBuilder();
            vote4.setColor(0x0087ff);
            vote4.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote4.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]);
            vote4.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote4.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            });
            vote4.clear();
            break;
            case 5:
            EmbedBuilder vote5 = new EmbedBuilder();
            vote5.setColor(0x0087ff);
            vote5.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote5.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]);
            vote5.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote5.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            });
            vote5.clear();
            break;
            case 6:
            EmbedBuilder vote6 = new EmbedBuilder();
            vote6.setColor(0x0087ff);
            vote6.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote6.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.5") + " " + variables[5]);
            vote6.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote6.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.5")).queue();
            });
            vote6.clear();
            break;
            case 7:
            EmbedBuilder vote7 = new EmbedBuilder();
            vote7.setColor(0x0087ff);
            vote7.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote7.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.5") + " " + variables[5]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.6") + " " + variables[6]);
            vote7.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote7.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.5")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.6")).queue();
            });
            vote7.clear();
            break;
            case 8:
            EmbedBuilder vote8 = new EmbedBuilder();
            vote8.setColor(0x0087ff);
            vote8.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote8.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.5") + " " + variables[5]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.6") + " " + variables[6]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.7") + " " + variables[7]);
            vote8.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote8.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.5")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.6")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.7")).queue();
            });
            vote8.clear();
            break;
            case 9:
            EmbedBuilder vote9 = new EmbedBuilder();
            vote9.setColor(0x0087ff);
            vote9.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote9.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.5") + " " + variables[5]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.6") + " " + variables[6]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.7") + " " + variables[7]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.8") + " " + variables[8]);
            vote9.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote9.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.5")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.6")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.7")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.8")).queue();
            });
            vote9.clear();
            break;
            case 10:
            EmbedBuilder vote10 = new EmbedBuilder();
            vote10.setColor(0x0087ff);
            vote10.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.vote") + variables[0] + TzeBot.essentials.LanguageDetector.getMessage("general.icon.question"));
            vote10.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1") + " " + variables[1]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.2") + " " + variables[2]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.3") + " " + variables[3]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.4") + " " + variables[4]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.5") + " " + variables[5]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.6") + " " + variables[6]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.7") + " " + variables[7]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.8") + " " + variables[8]
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.9") + " " + variables[9]);
            vote10.setFooter(TzeBot.essentials.LanguageDetector.getMessage("vote.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(vote10.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.1")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.2")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.3")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.4")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.5")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.6")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.7")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.8")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.9")).queue();
            });
            vote10.clear();
            default:
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("vote.error.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("vote.error.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            break;
        }
        }
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("vote.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("vote.gethelp");
    }
    
}

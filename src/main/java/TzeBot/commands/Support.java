/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.commands;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author Tzesh
 */
public class Support implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        
            EmbedBuilder support = new EmbedBuilder();
            support.setColor(0x00ff00);
            support.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.tzebot") + "TzeBot");
            support.setDescription(TzeBot.essentials.LanguageDetector.getMessage("support.setDescription")
                                    + "\n" + TzeBot.essentials.LanguageDetector.getMessage("support.setDescription2")
                                    + "\n" + TzeBot.essentials.LanguageDetector.getMessage("support.setDescription3")
                                    + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.patreon") + "https://www.patreon.com/tzebot"
                                    + "\n" + TzeBot.essentials.LanguageDetector.getMessage("support.discordbots") + "https://discordbotlist.com/bots/tzebot");
            support.setFooter(TzeBot.essentials.LanguageDetector.getMessage("support.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(support.build()).queue();
            support.clear();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("support.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("support.gethelp");
    }
    
    
}

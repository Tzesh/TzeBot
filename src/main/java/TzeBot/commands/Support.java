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

import static TzeBot.essentials.LanguageDetector.getMessage;


/**
 *
 * @author Tzesh
 */
public class Support implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        EmbedBuilder support = new EmbedBuilder();
        support.setColor(0x00ff00);
        support.setTitle(getMessage("general.icon.tzebot", guildID) + "TzeBot");
        support.setDescription(getMessage("support.setDescription", guildID)
                + "\n" + getMessage("support.setDescription2", guildID)
                + "\n" + getMessage("support.setDescription3", guildID)
                + "\n" + getMessage("general.icon.patreon", guildID) + "https://www.patreon.com/tzebot"
                + "\n" + getMessage("support.discordbots", guildID) + "https://discordbotlist.com/bots/tzebot" + "\nhttps://bots.discordlabs.org/bot/700416851678855168");
        support.setFooter(getMessage("support.setFooter", guildID));
        
        channel.sendMessage(support.build()).queue();
        support.clear();
    }

    @Override
    public String getName(long guildID) {
        return getMessage("support.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("support.gethelp", guildID);
    }

}

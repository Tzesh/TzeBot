package com.tzesh.tzebot.commands;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

/**
 * @author Tzesh
 */
public class Support implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        EmbedBuilder support = new EmbedBuilder();
        support.setColor(0x00ff00);
        support.setTitle(LanguageManager.getMessage("general.icon.tzebot", guildID) + " " + LanguageManager.getMessage("support.channel", guildID), "https://discord.com/invite/CY4pGVv");
        support.setDescription(LanguageManager.getMessage("support.setDescription", guildID)
                + "\n" + LanguageManager.getMessage("support.setDescription2", guildID)
                + "\n" + LanguageManager.getMessage("support.setDescription3", guildID)
                + "\n" + LanguageManager.getMessage("general.icon.patreon", guildID) + "https://www.patreon.com/tzebot"
                + "\n" + LanguageManager.getMessage("support.discordbots", guildID));
        support.addBlankField(true);
        support.addField("Discord Bot List", "https://top.gg/bot/700416851678855168", true);
        support.addBlankField(true);
        support.setFooter(LanguageManager.getMessage("support.setFooter", guildID));
        support.setTimestamp(Instant.now());

        channel.sendMessage(MessageCreateData.fromEmbeds(support.build())).queue();
        support.clear();
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("support.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("support.gethelp", guildID);
    }

}

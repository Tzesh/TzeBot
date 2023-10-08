package com.tzesh.tzebot.listeners.guild;

import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

/**
 * This is a simple class for event guild join listeners
 * @author tzesh
 */
public class GuildJoinEventListener extends AbstractEventListener<GuildJoinEvent> {
    @Override
    protected boolean canHandle(GuildJoinEvent event) {
        TextChannel defaultChannel = event.getGuild().getDefaultChannel().asTextChannel();
        boolean doesBotHavePermission = event.getGuild().getSelfMember().hasPermission(defaultChannel, Permission.MESSAGE_SEND);

        return doesBotHavePermission;
    }

    @Override
    protected void handle(GuildJoinEvent event) {
        TextChannel defaultChannel = event.getGuild().getDefaultChannel().asTextChannel();
        String guildName = event.getGuild().getName();

        LOGGER.info("Joined guild {}", guildName);

        defaultChannel.sendMessage("Greetings " + guildName + "," +
                "\n\uD83D\uDD33️ You can look at all categories of the commands of TzeBot by just typing `.help`" +
                "\n\uD83D\uDD33️ You can setup music channel which allows you to play songs without `.play` and use player with reactions (emotes)" +
                "\n\uD83D\uDD33️ Besides you can change language to Turkish by just typing: `.language Turkish`" +
                "\n\uD83D\uDD33️ Feel free to join our support channel if you encounter any kind of problems" +
                "\n\uD83D\uDD33️ You can always kick TzeBot to reset every single preference and data about your server" +
                "\n\uD83D\uDD33️ Thank you for choosing TzeBot").queue();
    }
}

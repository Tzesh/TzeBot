package com.tzesh.tzebot.listeners.guild;

import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

import static com.tzesh.tzebot.core.inventory.Inventory.*;

/**
 * This is a simple class for event guild leave listeners
 * @author tzesh
 */
public class GuildLeaveEventListener extends AbstractEventListener<GuildLeaveEvent> {
    @Override
    protected boolean canHandle(GuildLeaveEvent event) {
        return true;
    }

    @Override
    protected void handle(GuildLeaveEvent event) {
        long guildID = event.getGuild().getIdLong();

        LOGGER.info("Left guild {}", event.getGuild().getName());

        PREFIXES.remove(guildID);
        LANGUAGES.remove(guildID);
        EMOJI_CONTROLLED_MUSIC_CHANNELS.remove(guildID);
        INITIALIZED_MUSIC_CHANNELS.remove(guildID);
        VOTE_ROLE_CHANNELS.remove(guildID);
        VOLUMES.remove(guildID);
    }
}
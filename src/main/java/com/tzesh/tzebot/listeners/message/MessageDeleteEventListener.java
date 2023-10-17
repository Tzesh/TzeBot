package com.tzesh.tzebot.listeners.message;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

/**
 * A method to handle message deletion events
 * @author tzesh
 */
public class MessageDeleteEventListener extends AbstractEventListener<MessageDeleteEvent> {
    @Override
    protected void handle(MessageDeleteEvent event) {
        // get guild id and prefix
        final long guildId = event.getGuild().getIdLong();

        // get command guild channel
        final GuildChannel guildChannel = Inventory.get(guildId);

        // check if this is a vote message
        if (guildChannel.doesVoteRoleChannelExist() && guildChannel.getVoteRoleMessageID().equals(event.getMessageIdLong())) {
            guildChannel.setVoteRoleMessageID(null);
            guildChannel.setVoteRoleIDs(null);
            guildChannel.save();
            return;
        }

        // check is this is a music channel message
        if (guildChannel.doesMusicChannelExist() && guildChannel.getMusicChannelMessageID().equals(event.getMessageIdLong())) {
            guildChannel.setMusicChannelMessageID(null);
            guildChannel.setBoundedMusicChannelID(null);
            guildChannel.save();
        }
    }

    @Override
    protected boolean canHandle(MessageDeleteEvent event) {
        // get guild id and prefix
        final long guildId = event.getGuild().getIdLong();

        // get command guild channel
        final GuildChannel guildChannel = Inventory.get(guildId);

        // check if this is a vote message
        if (guildChannel.doesVoteRoleChannelExist() && guildChannel.getVoteRoleMessageID().equals(event.getMessageIdLong())) {
            return true;
        }

        // check is this is a music channel message
        return guildChannel.doesMusicChannelExist() && guildChannel.getMusicChannelMessageID() != null && guildChannel.getMusicChannelMessageID().equals(event.getMessageIdLong());
    }
}

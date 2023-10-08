package com.tzesh.tzebot.listeners.message;

import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;

import java.util.HashMap;

import static com.tzesh.tzebot.core.inventory.Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS;
import static com.tzesh.tzebot.core.inventory.Inventory.VOTE_ROLE_CHANNELS;

/**
 * A method to handle message deletion events
 * @author tzesh
 */
public class MessageDeleteEventListener extends AbstractEventListener<MessageDeleteEvent> {
    @Override
    protected void handle(MessageDeleteEvent event) {
        // check if this is a vote message
        if (VOTE_ROLE_CHANNELS.containsKey(event.getMessageIdLong())) {
            VOTE_ROLE_CHANNELS.remove(event.getMessageIdLong());
            return;
        }

        // check is this is a music channel message
        HashMap<Long, Long> IDs = EMOJI_CONTROLLED_MUSIC_CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null && IDs.containsValue(event.getMessageIdLong())) {
            IDs.values().remove(event.getMessageIdLong());
        }
    }

    @Override
    protected boolean canHandle(MessageDeleteEvent event) {
        // check if this is a vote message
        if (VOTE_ROLE_CHANNELS.containsKey(event.getMessageIdLong())) {
            return true;
        }

        // check is this is a music channel message
        HashMap<Long, Long> IDs = EMOJI_CONTROLLED_MUSIC_CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        return IDs != null && IDs.containsValue(event.getMessageIdLong());
    }
}

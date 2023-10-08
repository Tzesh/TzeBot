package com.tzesh.tzebot.listeners.message;

import com.tzesh.tzebot.core.CommandManager;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

import static com.tzesh.tzebot.core.common.CommonConstants.DEFAULT_PREFIX;
import static com.tzesh.tzebot.core.inventory.Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS;
import static com.tzesh.tzebot.core.inventory.Inventory.PREFIXES;

/**
 * A method for handling message reactions to commands
 * @author tzesh
 */
public class MessageReceivedEventListener extends AbstractEventListener<MessageReceivedEvent> {

    @Override
    protected void handle(MessageReceivedEvent event) {
        // get guild id and prefix
        final long guildId = event.getGuild().getIdLong();
        final String prefix = PREFIXES.computeIfAbsent(guildId, (id) -> DEFAULT_PREFIX);

        // check if this is a music channel message
        HashMap<Long, Long> IDs = EMOJI_CONTROLLED_MUSIC_CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null && IDs.containsKey(event.getChannel().getIdLong())) {
            // handle music channel message
            new CommandManager(guildId, prefix).handleCommand(event);
            return;
        }

        // handle command
        new CommandManager(guildId, prefix).handleCommand(event);
    }

    @Override
    protected boolean canHandle(MessageReceivedEvent event) {
        // get raw message and user
        String raw = event.getMessage().getContentRaw();
        User user = event.getAuthor();

        // check if user is bot or webhook
        if (user.isBot() || event.isWebhookMessage()) {
            return false;
        }

        // check if this is a music channel message
        HashMap<Long, Long> IDs = EMOJI_CONTROLLED_MUSIC_CHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null && IDs.containsKey(event.getChannel().getIdLong())) {
            return true;
        }
        // check if this is a command
        else {
            final long guildId = event.getGuild().getIdLong();
            String pre = PREFIXES.computeIfAbsent(guildId, (id) -> ConfigurationManager.getEnvKey("pre"));

            return raw.startsWith(pre);
        }
    }
}

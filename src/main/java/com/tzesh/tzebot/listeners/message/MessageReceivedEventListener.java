package com.tzesh.tzebot.listeners.message;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.command.CommandManager;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A method for handling message reactions to commands
 * @author tzesh
 */
public class MessageReceivedEventListener extends AbstractEventListener<MessageReceivedEvent> {

    @Override
    protected void handle(MessageReceivedEvent event) {
        // get guild id and prefix
        final long guildId = event.getGuild().getIdLong();

        // get command guild channel
        final GuildChannel guildChannel = Inventory.get(guildId);

        // get command manager and handle command
        new CommandManager(guildChannel).handleCommand(event);
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

        // get guild id and prefix
        final long guildId = event.getGuild().getIdLong();

        // get command guild channel
         final GuildChannel guildChannel = Inventory.get(guildId);

        // check if this is a music channel message
        if (guildChannel.doesMusicChannelExist() && guildChannel.getBoundedMusicChannelID().equals(event.getChannel().getIdLong())) {
            return true;
        }
        // check if this is a command
        else {
            return raw.startsWith(guildChannel.getPrefix());
        }
    }
}

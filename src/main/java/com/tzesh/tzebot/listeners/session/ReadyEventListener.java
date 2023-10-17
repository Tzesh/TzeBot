package com.tzesh.tzebot.listeners.session;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

/**
 * This is a simple event listener for ready events
 * @author tzesh
 */
public class ReadyEventListener extends AbstractEventListener<ReadyEvent> {

    @Override
    protected boolean canHandle(ReadyEvent event) {
        return true;
    }

    @Override
    protected void handle(ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getName());
        LOGGER.info("Regenerating music channels...");
        regenerateMusicChannels(event);
    }

    private void regenerateMusicChannels(ReadyEvent event) {
        Inventory.GUILD_CHANNELS.keySet().forEach(guildID -> {
            GuildChannel guildChannel = Inventory.get(guildID);
            if (guildChannel.doesMusicChannelExist()) {
                initializeMusicChannel(event, guildChannel);
            }
        });
    }

    private void initializeMusicChannel(ReadyEvent event, GuildChannel guildChannel) {
        TextChannel channel = event.getJDA().getTextChannelById(guildChannel.getBoundedMusicChannelID());
        MessageEmbed embedMessage = EmbedMessageBuilder.createMusicChannelEmbeddedMessage(guildChannel);
        MessageEditData editData = MessageEditData.fromEmbeds(embedMessage);

        if (channel != null) {
            channel.editMessageById(guildChannel.getMusicChannelMessageID(), editData).queue(EmbedMessageBuilder::initializeMusicChannelEmojiControls);
        }
    }
}

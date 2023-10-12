package com.tzesh.tzebot.listeners.session;

import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.util.HashMap;
import java.util.Map;

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
        Inventory.INITIALIZED_MUSIC_CHANNELS.keySet().forEach(guildID -> {
            Map<Long, Long> channelInformation = Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS.getOrDefault(guildID, new HashMap<>());
            if (channelInformation.size() != 0) {
                initializeMusicChannel(guildID, event, channelInformation);
            }
        });
    }

    private void initializeMusicChannel(Long guildID, ReadyEvent event, Map<Long, Long> channelInformation) {
        channelInformation.forEach((channelID, messageID) -> {
            TextChannel channel = event.getJDA().getTextChannelById(channelID);
            MessageEmbed embedMessage = EmbedMessageBuilder.createMusicChannelEmbeddedMessage(channelID);
            MessageEditData editData = MessageEditData.fromEmbeds(embedMessage);

            if (channel != null) {
                channel.editMessageById(messageID, editData).queue(EmbedMessageBuilder::initializeMusicChannelEmojiControls);
            } else {
                Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS.remove(guildID);
                Inventory.INITIALIZED_MUSIC_CHANNELS.remove(guildID);
            }
        });
    }
}

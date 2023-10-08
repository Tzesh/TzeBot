package com.tzesh.tzebot.listeners.session;

import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import com.tzesh.tzebot.core.music.enums.MusicEmoteUnicodes;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
        for (Long guildID : Inventory.INITIALIZED_MUSIC_CHANNELS.keySet()) {
            HashMap<Long, Long> IDs = Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS.get(guildID);
            for (Long channelID : IDs.keySet()) {
                Long messageID = IDs.get(channelID);
                TextChannel channel = event.getJDA().getTextChannelById(channelID);
                MessageEmbed embedMessage = EmbedMessageBuilder.createMusicChannelEmbeddedMessage(guildID);
                MessageEditData editData = MessageEditData.fromEmbeds(embedMessage);
                if (channel != null) {
                    channel.editMessageById(messageID, editData).queueAfter(1000, TimeUnit.MILLISECONDS, message -> {
                        if (message.getReactions().size() > 0) message.clearReactions().queue();
                        message.addReaction(MusicEmoteUnicodes.nowPlaying.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.stop.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.skip.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.loop.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.shuffle.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.next.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.previous.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.volumedown.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.volumeup.getUnicode()).queue();
                        message.addReaction(MusicEmoteUnicodes.queue.getUnicode()).queue();
                    });
                }
            }
        }
    }
}

package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;

/**
 * A class that represents the queue command
 * @author tzesh
 * @see Command
 */
public class Queue<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isAppropriate = queue.isEmpty() && audioPlayer.getPlayingTrack() == null;
        addPreRequisite(!isAppropriate, "loop.error.setTitle", "loop.error.setDescription");
    }

    @Override
    public void handleCommand() {
        sendMessage(EmbedMessageBuilder.createNowPlayingEmbeddedMessage(audioPlayer, user, guildID));
        sendMessage(EmbedMessageBuilder.createQueueEmbeddedMessage(queue, user, guildID));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("queue.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("queue.gethelp", guildID);
    }
}

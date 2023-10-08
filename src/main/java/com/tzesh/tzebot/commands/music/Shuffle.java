package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;


/**
 * A class that represents the shuffle command
 *
 * @author tzesh
 */
public class Shuffle<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isQueueEmptyAndNotPlaying = musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null;
        addPreRequisite(!isQueueEmptyAndNotPlaying, "loop.error.setTitle", "loop.error.setDescription");
    }

    @Override
    public void handleCommand() {
        scheduler.shufflePlaylist();
        sendMessage(EmbedMessageBuilder.createSuccessMessage("shuffle.success.setTitle", "", user, guildID));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("shuffle.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("shuffle.gethelp", guildID);
    }

}

package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;

/**
 * A class to manage the loop command
 * @author tzesh
 */
public class Loop<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isQueueEmpty = musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null;
        addPreRequisite(!isQueueEmpty, "loop.error.setTitle", "loop.error.setDescription");
    }

    @Override
    public void handleCommand() {
        boolean isRepeating = scheduler.isRepeating();
        scheduler.setRepeating(!isRepeating);
        String title = isRepeating ? "loop.success.off.setTitle" : "loop.success.on.setTitle";
        sendMessage(EmbedMessageBuilder.createSuccessMessage(title, "", user, guildID));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("loop.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("loop.gethelp", guildID);
    }
}

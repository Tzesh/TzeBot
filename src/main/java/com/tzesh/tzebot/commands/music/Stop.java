package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;

/**
 * A class that represents the stop command
 * @author tzesh
 */
public class Stop<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPaused = musicManager.player.isPaused();
        addPreRequisite(!isPaused, "stop.error.setTitle", "stop.error.setDescription");
    }

    @Override
    public void handleCommand() {

        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            sendMessage(EmbedMessageBuilder.createSuccessMessage("leave.success.setTitle", "", user, guildID));
        }

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        sendMessage(EmbedMessageBuilder.createSuccessMessage("stop.success.setTitle", "", user, guildID));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("stop.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("stop.gethelp", guildID);
    }
}

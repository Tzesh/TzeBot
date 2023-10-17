package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;

/**
 * A class to manage the resume command
 * @author tzesh
 */
public class Resume<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPlaying = audioPlayer.getPlayingTrack() != null;
        addPreRequisite(isPlaying, "nowplaying.error.setTitle", "resume.error.setDescription");
        if (!isPlaying) return;

        boolean isPaused = audioPlayer.isPaused();
        addPreRequisite(isPaused, "nowplaying.error.setTitle", "resume.nothingtoresumed.setDescription");
    }

    @Override
    public void handleCommand() {
        audioPlayer.setPaused(false);
    }

    @Override
    public String getName(long guildID) {
        return getMessage("resume.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("resume.gethelp", guildID);
    }
}

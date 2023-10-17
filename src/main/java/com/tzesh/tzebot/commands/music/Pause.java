package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;

/**
 * A class to manage the pause command
 * @author tzesh
 */
public class Pause<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPlaying = audioPlayer.getPlayingTrack() != null;
        addPreRequisite(isPlaying, "pause.error.setTitle", "pause.error.setDescription");
    }

    @Override
    public void handleCommand() {
        audioPlayer.setPaused(true);
        String title = getMessage("general.icon.pause", this.guildChannel.getLanguage()) + getMessage("pause.success.setTitle", this.guildChannel.getLanguage()) + audioTrackInfo.title;

        sendMessage(EmbedMessageBuilder.createCustomSuccessMessage(title, "", user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("pause.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("pause.gethelp", guildID);
    }

}

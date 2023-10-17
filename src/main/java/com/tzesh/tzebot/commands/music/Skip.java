package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;

public class Skip<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPlaying = audioPlayer.getPlayingTrack() != null;
        addPreRequisite(isPlaying, "skip.error.setTitle", "skip.error.setDescription");
    }

    @Override
    public void handleCommand() {
        scheduler.nextTrack();
        String title = getMessage("skip.success.setTitle1", this.guildChannel.getLanguage()) + audioTrackInfo.title + getMessage("skip.success.setTitle2", this.guildChannel.getLanguage());
        sendMessage(EmbedMessageBuilder.createCustomSuccessMessage(title, "", user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("skip.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("skip.gethelp", guildID);
    }
}

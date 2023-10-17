package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;

/**
 * A class to manage the now playing command
 * @author tzesh
 */
public class NowPlaying<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPlaying = audioPlayer.getPlayingTrack() != null;
        addPreRequisite(isPlaying, "nowplaying.error.setTitle", "nowplaying.error.setDescription");
    }

    @Override
    public void handleCommand() {
        sendMessage(EmbedMessageBuilder.createNowPlayingEmbeddedMessage(audioPlayer, user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("nowplaying.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("nowplaying.gethelp", guildID);
    }
}

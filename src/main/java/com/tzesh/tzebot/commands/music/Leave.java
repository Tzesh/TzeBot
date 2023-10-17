package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;

/**
 * A class to manage the leave command
 * @author tzesh
 */
public class Leave<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isConnected = audioManager.isConnected();
        addPreRequisite(isConnected, "leave.cannotleave.setTitle", "leave.notin");
        if (!isConnected) return;

        boolean memberIsInVoiceChannel = voiceChannel.getMembers().contains(member);
        addPreRequisite(memberIsInVoiceChannel, "leave.cannotleave.setTitle", "leave.notin");
    }

    @Override
    public void handleCommand() {
        if (audioPlayer.getPlayingTrack() != null) {
            musicManager.stopAndClearQueue();

            sendMessage(EmbedMessageBuilder.createSuccessMessage("stop.success.setTitle", "", user, this.guildChannel));
        }

        audioManager.closeAudioConnection();
        sendMessage(EmbedMessageBuilder.createSuccessMessage("leave.success.setTitle", "", user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("leave.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("leave.gethelp", guildID);
    }
}

package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.core.music.client.YouTubeClient;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;
import static com.tzesh.tzebot.core.music.constants.MusicCommonConstants.DEFAULT_VOLUME;
import static com.tzesh.tzebot.utils.InputControlHelper.isUrl;

/**
 * A class to manage the play command
 *
 * @author tzesh
 */
public class Play<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    private final YouTubeClient youTube = new YouTubeClient();
    private String searchResult;

    @Override
    protected void initializePreRequisites() {
        if (this.isBounded) this.message.delete().queue();

        boolean isMemberInAudioChannel = memberVoiceState.inAudioChannel() && voiceChannel != null;
        addPreRequisite(isMemberInAudioChannel, "join.joinchannel.setTitle", "join.joinchannel.setDescription");
        if (!isMemberInAudioChannel) return;

        boolean isSelfMemberHasPermission = selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT) && selfMember.hasPermission(voiceChannel, Permission.VOICE_SPEAK);
        addPreRequisite(isSelfMemberHasPermission, "join.cannotjoin.setTitle", "join.cannotjoin.setDescription");
        if (!isSelfMemberHasPermission) return;

        this.searchResult = youTube.searchAudio(rawMessage);
        boolean isFound = isUrl(rawMessage) || searchResult != null;
        addPreRequisite(isFound, "play.noresults.setTitle", "play.noresults.setDescription");
    }

    @Override
    public void handleCommand() {
        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(voiceChannel);
            audioManager.setSelfDeafened(true);
            audioPlayer.setVolume(volume);

            sendMessage(EmbedMessageBuilder.createSuccessMessage("join.success.setTitle", "", user, guildID));
        }

        if (audioPlayer.isPaused()) audioPlayer.setPaused(false);
        if (audioPlayer.getVolume() == 0) audioPlayer.setVolume(DEFAULT_VOLUME);
        playerManager.loadAndPlay(channel, searchResult, user, this.isBounded, guildID);
    }

    @Override
    public String getName(long guildID) {
        return getMessage("play.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("play.gethelp1", guildID) + "\n"
                + getMessage("play.gethelp2", guildID) + ConfigurationManager.getEnvKey("pre") + getName(guildID) + getMessage("play.gethelp3", guildID);
    }
}

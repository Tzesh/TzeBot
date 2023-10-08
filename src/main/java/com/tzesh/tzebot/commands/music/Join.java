package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;

/**
 * A class to manage the join command
 * @author tzesh
 */
public class Join<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isNotConnected = !audioManager.isConnected();
        addPreRequisite(isNotConnected, "join.alreadyconnected.setTitle", "join.alreadyconnected.setDescription");
        if (!isNotConnected) return;

        boolean memberIsInVoiceChannel = memberVoiceState.inAudioChannel();
        addPreRequisite(memberIsInVoiceChannel, "join.joinchannel.setTitle", "join.joinchannel.setDescription");
        if (!memberIsInVoiceChannel) return;

        boolean selfMemberHasPermission = selfMember.hasPermission(Permission.VOICE_CONNECT) && selfMember.hasPermission(Permission.VOICE_SPEAK);
        addPreRequisite(selfMemberHasPermission, "join.cannotjoin.setTitle", "join.cannotjoin.setDescription");
    }

    @Override
    public void handleCommand() {
        audioManager.openAudioConnection(voiceChannel);
        audioManager.setSelfDeafened(true);
        audioPlayer.setVolume(volume);

        sendMessage(EmbedMessageBuilder.createSuccessMessage("join.success.setTitle", "", user, guildID));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("join.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("join.gethelp", guildID);
    }
}

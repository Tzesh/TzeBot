package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;
import static com.tzesh.tzebot.core.music.constants.MusicCommonConstants.FORWARD_AMOUNT;

/**
 * A class that represents the seek command
 * @author tzesh
 */
public class Seek<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isPlaying = audioPlayer.getPlayingTrack() != null;
        addPreRequisite(isPlaying, "nowplaying.error.setTitle", "nowplaying.error.setDescription");
        if (!isPlaying) return;

        boolean isArgsCorrect = args.size() == 1 && (args.get(0).equals(getMessage("seek.forward", guildID)) || args.get(0).equals(getMessage("seek.backward", guildID)));
        addPreRequisite(isArgsCorrect, "seek.error.setTitle", "seek.error.setDescription");
    }

    @Override
    public void handleCommand() {
        boolean isForward = args.get(0).equals(getMessage("seek.forward", guildID));
        int amount = isForward ? FORWARD_AMOUNT : -FORWARD_AMOUNT;
        scheduler.changePosition(amount);
    }

    @Override
    public String getName(long guildID) {
        return getMessage("seek.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("seek.gethelp", guildID).replace(".", prefix);
    }
}

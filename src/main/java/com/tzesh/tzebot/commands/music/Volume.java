package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.InputControlHelper.isInteger;

/**
 * A class that represents the volume command
 *
 * @author tzesh
 */
public class Volume<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isArgsAppropriate = args.size() == 1;
        addPreRequisite(isArgsAppropriate, "general.403", "general.403.description");
        if (!isArgsAppropriate) return;

        boolean isAppropriate = isInteger(args.get(0)) && Integer.parseInt(args.get(0)) <= 100 && Integer.parseInt(args.get(0)) >= 0;
        addPreRequisite(isAppropriate, "volume.error.setTitle", "");
    }

    @Override
    public void handleCommand() {
        int desiredVolume = Integer.parseInt(args.get(0));
        audioPlayer.setVolume(desiredVolume);

        this.guildChannel.setVolume(desiredVolume);
        this.guildChannel.save();

        sendMessage(EmbedMessageBuilder.createCustomSuccessMessage(getMessage("volume.success.setTitle", this.guildChannel.getLanguage()) + desiredVolume, "", user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return getMessage("volume.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("volume.gethelp", guildID);
    }
}

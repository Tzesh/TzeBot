package com.tzesh.tzebot.utils;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.command.CommandContextImpl;
import com.tzesh.tzebot.core.inventory.Inventory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class contains some useful methods for input control
 * @author tzesh
 */
public class InputControlHelper {

    public static boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isMusicChannelPresent(CommandContextImpl commandContext) {
        final long guildID = commandContext.getGuild().getIdLong();
        final GuildChannel guildChannel = Inventory.get(guildID);

        return guildChannel.doesMusicChannelExist() && guildChannel.getMusicChannelMessageID() != null;
    }

    public static boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    public static boolean isYouTubeUrl(String input) {
        if (!isUrl(input)) return false;
        return input.contains("youtube.com") || input.contains("youtu.be");
    }
}

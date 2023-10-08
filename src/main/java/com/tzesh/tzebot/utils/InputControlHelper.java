package com.tzesh.tzebot.utils;

import com.tzesh.tzebot.core.CommandContextImpl;
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
        long guildID = commandContext.getGuild().getIdLong();

        if (Inventory.INITIALIZED_MUSIC_CHANNELS.containsKey(guildID)) {
            return commandContext.getGuild().getChannels().contains(commandContext.getGuild().getTextChannelById(Inventory.INITIALIZED_MUSIC_CHANNELS.get(guildID)));
        }

        return false;
    }

    public static boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }
}

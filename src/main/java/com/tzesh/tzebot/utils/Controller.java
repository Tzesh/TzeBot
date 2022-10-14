package com.tzesh.tzebot.utils;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.Config;

public class Controller {

    public static boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isExists(CommandContext ctx) {
        if (Config.CHANNELCREATED.containsKey(ctx.getGuild().getIdLong())) {
            return ctx.getGuild().getChannels().contains(ctx.getGuild().getTextChannelById(Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())));
        }
        return false;
    }
}

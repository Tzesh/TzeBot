package com.tzesh.tzebot.core.inventory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This is a simple class for inventory of the bot could be replaced by a database but, I don't want to use a database for this bot
 * especially it is a small bot and must be ready to go for everyone. So I decided to use a simple inventory for the bot.
 * @author tzesh
 */
public class Inventory {
    public static Map<Long, String> PREFIXES = new HashMap<>(); // All the prefixes of the servers default is .env's prefix setting
    public static Map<Long, String> LANGUAGES = new HashMap<>(); // All the languages of the servers default is English
    public static Map<Long, LinkedList<Long>> VOTE_ROLE_CHANNELS = new HashMap<>(); // Vote role commands, we don't want to lose messages and role data when we restart the bot.
    public static Map<Long, HashMap<Long, Long>> EMOJI_CONTROLLED_MUSIC_CHANNELS = new HashMap<>(); // Music channels that are created and initialized.
    public static Map<Long, Integer> VOLUMES = new HashMap<>(); // Volumes of the servers default is 50%
    public static Map<Long, Long> INITIALIZED_MUSIC_CHANNELS = new HashMap<>(); // Music channels that are created but either initialized or not. It's important value for preventing some kind of abusing of channel creation.
}
package com.tzesh.tzebot.core.inventory;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.strategy.LocalStoreStrategy;
import com.tzesh.tzebot.core.inventory.strategy.abstracts.StoreStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple class for inventory of the bot could be replaced by a database but, I don't want to use a database for this bot
 * especially it is a small bot and must be ready to go for everyone. So I decided to use a simple inventory for the bot.
 * @author tzesh
 */
public class Inventory {
    public static StoreStrategy<Long, GuildChannel> inventoryStrategy = new LocalStoreStrategy();
    public static Map<Long, GuildChannel> GUILD_CHANNELS = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(Inventory.class);

    public static GuildChannel get(Long guildID) {
        return inventoryStrategy.get(guildID);
    }

    public static void save(GuildChannel guildChannel) {
        inventoryStrategy.save(guildChannel);
    }

    public static void remove(Long guildID) {
        LOGGER.info("Removing guild channel with id: " + guildID);
        inventoryStrategy.remove(get(guildID));
    }

    public static void setInventoryStrategy(StoreStrategy<Long, GuildChannel> inventoryStrategy) {
        LOGGER.info("Setting inventory strategy to: " + inventoryStrategy.getClass().getSimpleName());
        Inventory.inventoryStrategy = inventoryStrategy;
    }
}
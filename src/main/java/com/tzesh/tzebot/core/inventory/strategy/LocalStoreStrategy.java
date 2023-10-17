package com.tzesh.tzebot.core.inventory.strategy;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.channel.impl.GuildChannelImpl;
import com.tzesh.tzebot.core.inventory.strategy.abstracts.StoreStrategy;

import static com.tzesh.tzebot.core.inventory.Inventory.GUILD_CHANNELS;

/**
 * Local saving strategy for guild channels
 * @author tzesh
 */
public class LocalStoreStrategy implements StoreStrategy<Long, GuildChannel> {
    @Override
    public void save(GuildChannel object) {
        GUILD_CHANNELS.put(object.getGuildID(), object);
    }

    @Override
    public void remove(GuildChannel object) {
        if (this.contains(object.getGuildID()))
            GUILD_CHANNELS.remove(object.getGuildID());
    }

    @Override
    public GuildChannel get(Long key) {
        GuildChannel guildChannel = GUILD_CHANNELS.getOrDefault(key, null);

        if (guildChannel == null) {
            guildChannel = new GuildChannelImpl(key);
            GUILD_CHANNELS.put(key, guildChannel);
        }

        return guildChannel;
    }

    @Override
    public boolean contains(Long key) {
        return GUILD_CHANNELS.containsKey(key);
    }
}

package com.tzesh.tzebot.core.inventory.strategy;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.channel.impl.GuildChannelImpl;
import com.tzesh.tzebot.core.database.impl.MongoChannelManager;
import com.tzesh.tzebot.core.inventory.strategy.abstracts.StoreStrategy;

/**
 * MongoDB saving strategy for guild channels
 * @author tzesh
 */
public class MongoDBStoreStrategy implements StoreStrategy<Long, GuildChannel> {
    private final MongoChannelManager mongoChannelManager = MongoChannelManager.getInstance();
    @Override
    public void save(GuildChannel object) {
        this.mongoChannelManager.save((GuildChannelImpl) object);
    }

    @Override
    public void remove(GuildChannel object) {
        this.mongoChannelManager.delete(object.getGuildID());
    }

    @Override
    public GuildChannel get(Long key) {
        return this.mongoChannelManager.get(key);
    }

    @Override
    public boolean contains(Long key) {
        return this.mongoChannelManager.exists(key);
    }
}

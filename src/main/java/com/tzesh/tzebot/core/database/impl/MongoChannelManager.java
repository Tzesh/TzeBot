package com.tzesh.tzebot.core.database.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.channel.impl.GuildChannelImpl;
import com.tzesh.tzebot.core.database.abstracts.AbstractDatabaseManager;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.pojo.Conventions.ANNOTATION_CONVENTION;

/**
 * This manager is used to manage the database for guild channels
 * @author tzesh
 */
public class MongoChannelManager extends AbstractDatabaseManager<MongoClient, Long, GuildChannelImpl> {
    private volatile static MongoChannelManager instance = null;

    public MongoChannelManager() {
        super();
    }

    public synchronized static MongoChannelManager getInstance() {
        if (instance == null) {
            return new MongoChannelManager();
        }

        return instance;
    }

    @Override
    public MongoClient initializeClient() {
        ConnectionString connectionString = new ConnectionString(this.URI);
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);


        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();


        return MongoClients.create(clientSettings);
    }

    @Override
    public void save(GuildChannelImpl object) {
        if (this.exists(object.getGuildID())) {
            this.update(object.getGuildID(), object);
            return;
        }

        this.getCollection().insertOne(object);
    }

    @Override
    public GuildChannelImpl get(Long key) {
        GuildChannelImpl guildChannel = this.getCollection().find(eq("guild_id", key)).first();

        if (guildChannel == null) {
            guildChannel = new GuildChannelImpl(key);
            this.getCollection().insertOne(guildChannel);
        }

        return guildChannel;
    }

    @Override
    public void delete(Long key) {
        this.getCollection().deleteOne(eq("guild_id", key));
    }

    @Override
    public void update(Long key, GuildChannelImpl object) {
        this.getCollection().replaceOne(eq("guild_id", key), object);
    }

    @Override
    public boolean exists(Long key) {
        return get(key) != null;
    }

    private MongoCollection<GuildChannelImpl> getCollection() {
        return this.client.getDatabase(this.databaseName).getCollection(this.collectionName, GuildChannelImpl.class);
    }
}

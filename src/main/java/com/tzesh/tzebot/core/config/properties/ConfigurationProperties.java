package com.tzesh.tzebot.core.config.properties;

import com.tzesh.tzebot.core.config.ConfigurationManager;

/**
 * This class contains all the properties of the bot .env file
 * @author tzesh
 */
public class ConfigurationProperties {
    private final String token;
    private final String prefix;
    private final String owner;
    private final String key;
    private final String shards;
    private final boolean useMongoDB;
    private final String mongoDBURI;
    private final String mongoDBDatabase;
    private final String mongoDBCollection;

    public ConfigurationProperties() {
        this.token = ConfigurationManager.getEnvKey("token");
        this.prefix = ConfigurationManager.getEnvKey("pre");
        this.owner = ConfigurationManager.getEnvKey("owner");
        this.key = ConfigurationManager.getEnvKey("key");
        this.shards = ConfigurationManager.getEnvKey("shard");
        this.useMongoDB = Boolean.parseBoolean(ConfigurationManager.getEnvKey("use_mongodb"));
        this.mongoDBURI = ConfigurationManager.getEnvKey("mongodb_uri");
        this.mongoDBDatabase = ConfigurationManager.getEnvKey("mongodb_database");
        this.mongoDBCollection = ConfigurationManager.getEnvKey("mongodb_collection");
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getOwner() {
        return owner;
    }

    public String getKey() {
        return key;
    }

    public String getShards() {
        return shards;
    }

    public boolean isUseMongoDB() {
        return useMongoDB;
    }

    public String getMongoDBURI() {
        return mongoDBURI;
    }

    public String getMongoDBDatabase() {
        return mongoDBDatabase;
    }

    public String getMongoDBCollection() {
        return mongoDBCollection;
    }
}

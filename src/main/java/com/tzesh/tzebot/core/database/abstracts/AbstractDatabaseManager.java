package com.tzesh.tzebot.core.database.abstracts;

import com.tzesh.tzebot.core.config.properties.ConfigurationProperties;

/**
 * This is a simple class for database managers
 * @author tzesh
 */
public abstract class AbstractDatabaseManager<C, T, K> implements DatabaseManager<T, K> {
    protected final C client;
    protected final String URI;
    protected final String databaseName;
    protected final String collectionName;

    public AbstractDatabaseManager() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        this.URI = configurationProperties.getMongoDBURI();
        this.databaseName = configurationProperties.getMongoDBDatabase();
        this.collectionName = configurationProperties.getMongoDBCollection();
        this.client = initializeClient();
    }

    protected abstract C initializeClient();
}

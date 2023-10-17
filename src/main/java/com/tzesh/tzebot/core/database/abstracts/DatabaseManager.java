package com.tzesh.tzebot.core.database.abstracts;

/**
 * This is a simple interface for database managers
 * @author tzesh
 */
public interface DatabaseManager<K, V> {
    void save(V object);

    V get(K key);

    void delete(K key);

    void update(K key, V object);

    boolean exists(K key);

}

package com.tzesh.tzebot.core.inventory.strategy.abstracts;

/**
 * This is a simple interface for saving strategies
 * @author tzesh
 */
public interface StoreStrategy<K, V> {
    void save(V object);
    void remove(V object);
    V get(K key);
    boolean contains(K key);
}

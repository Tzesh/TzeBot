package com.tzesh.tzebot.listeners.abstracts;

import net.dv8tion.jda.api.events.Event;

/**
 * This is a simple interface for event listeners
 * @param <T> The event type
 * @author tzesh
 */
public interface EventListener<T extends Event> {
    void onEvent(T event);
}

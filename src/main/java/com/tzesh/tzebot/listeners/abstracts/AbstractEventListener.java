package com.tzesh.tzebot.listeners.abstracts;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple abstract class for event listeners
 * @author tzesh
 */
public abstract class AbstractEventListener<T extends Event> implements EventListener<T> {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void onEvent(T event) {
        if (canHandle(event)) {
            handle(event);
        }
    }

    protected abstract void handle(T event);

    protected abstract boolean canHandle(T event);
}

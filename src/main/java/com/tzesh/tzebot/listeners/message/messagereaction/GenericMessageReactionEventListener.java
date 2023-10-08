package com.tzesh.tzebot.listeners.message.messagereaction;

import com.tzesh.tzebot.core.LanguageManager;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

/**
 * This is a simple abstract class for event message reaction listeners
 * @author tzesh
 */
public abstract class GenericMessageReactionEventListener<T extends GenericMessageReactionEvent> extends AbstractEventListener<T> {
    @Override
    protected boolean canHandle(T event) {
        boolean isBot = event.getUser().isBot();

        return !isBot;
    }

    protected String getFormattedEmoji(T event) {
        return event.getReaction().getEmoji().getFormatted();
    }

    protected int getRoleIndexByEmoji(String emoji, int size) {
        String baseKey = "general.icon.";

        for (int i = 0; i < size; i++) {
            if (LanguageManager.getMessage(baseKey + i).equals(emoji)) {
                return i;
            }
        }

        return -1;
    }
}

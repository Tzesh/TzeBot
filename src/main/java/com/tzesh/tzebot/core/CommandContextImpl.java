package com.tzesh.tzebot.core;

import com.tzesh.tzebot.commands.abstracts.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandContextImpl<T extends GenericMessageEvent> implements CommandContext<T> {

    private final T event;
    private final List<String> args;

    public CommandContextImpl(T event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public T getEvent() {
        return this.event;
    }

    @Override
    public List<String> getArgs() {
        return this.args;
    }
}

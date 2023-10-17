package com.tzesh.tzebot.core.command;

import com.tzesh.tzebot.commands.abstracts.CommandContext;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.List;

public class CommandContextImpl<T extends GenericMessageEvent> implements CommandContext<T> {

    private final T event;
    private final List<String> args;
    private final GuildChannel guildChannel;

    public CommandContextImpl(T event, List<String> args, GuildChannel guildChannel) {
        this.event = event;
        this.args = args;
        this.guildChannel = guildChannel;
    }

    @Override
    public GuildChannel getGuildChannel() {
        return this.guildChannel;
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

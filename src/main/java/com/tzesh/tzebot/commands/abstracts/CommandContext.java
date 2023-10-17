package com.tzesh.tzebot.commands.abstracts;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.List;

/**
 * An interface for commands context
 * @author tzesh
 * @see Command
 * @see AbstractCommand
 */
public interface CommandContext<T extends GenericMessageEvent> {

    Guild getGuild();

    T getEvent();

    List<String> getArgs();

    GuildChannel getGuildChannel();

    default TextChannel getChannel() {
        return this.getEvent().getGuildChannel().asTextChannel();
    }

    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }
}

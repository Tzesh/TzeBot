package com.tzesh.tzebot.essentials;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ICommandContext {

    Guild getGuild();

    MessageReceivedEvent getEvent();

    default TextChannel getChannel() {
        return this.getEvent().getGuildChannel().asTextChannel();
    }

    default Message getMessage() {
        return this.getEvent().getMessage();
    }

    default Member getMember() {
        return this.getEvent().getMember();
    }

    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }
}

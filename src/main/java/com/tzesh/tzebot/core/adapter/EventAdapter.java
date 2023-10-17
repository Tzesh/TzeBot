package com.tzesh.tzebot.core.adapter;

import com.tzesh.tzebot.listeners.guild.GuildJoinEventListener;
import com.tzesh.tzebot.listeners.guild.GuildLeaveEventListener;
import com.tzesh.tzebot.listeners.guild.voice.VoiceUpdateListener;
import com.tzesh.tzebot.listeners.message.MessageDeleteEventListener;
import com.tzesh.tzebot.listeners.message.MessageReceivedEventListener;
import com.tzesh.tzebot.listeners.message.messagereaction.music.MusicChannelReactionListener;
import com.tzesh.tzebot.listeners.message.messagereaction.voterole.AddRoleEventListener;
import com.tzesh.tzebot.listeners.message.messagereaction.voterole.RemoveRoleEventListener;
import com.tzesh.tzebot.listeners.session.ReadyEventListener;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * This is an adapter class for event listeners
 * It is used to register all event listeners in one place
 * @author tzesh
 */
public class EventAdapter extends ListenerAdapter {
    private final AddRoleEventListener addRoleEventListener = new AddRoleEventListener();
    private final RemoveRoleEventListener removeRoleEventListener = new RemoveRoleEventListener();
    private final ReadyEventListener readyEventListener = new ReadyEventListener();
    private final GuildLeaveEventListener guildLeaveEventListener = new GuildLeaveEventListener();
    private final GuildJoinEventListener guildJoinEventListener = new GuildJoinEventListener();
    private final VoiceUpdateListener voiceUpdateListener = new VoiceUpdateListener();
    private final MessageDeleteEventListener messageDeleteEventListener = new MessageDeleteEventListener();
    private final MessageReceivedEventListener messageReceivedEventListener = new MessageReceivedEventListener();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        readyEventListener.onEvent(event);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        this.messageReceivedEventListener.onEvent(event);
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        this.messageDeleteEventListener.onEvent(event);
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        new MusicChannelReactionListener(event).onEvent(event);
        this.addRoleEventListener.onEvent(event);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        this.removeRoleEventListener.onEvent(event);
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        voiceUpdateListener.onEvent(event);
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        guildJoinEventListener.onEvent(event);
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        guildLeaveEventListener.onEvent(event);
    }
}

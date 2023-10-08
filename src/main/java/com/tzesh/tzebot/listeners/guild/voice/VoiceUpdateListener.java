package com.tzesh.tzebot.listeners.guild.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.tzesh.tzebot.listeners.abstracts.AbstractEventListener;
import com.tzesh.tzebot.core.music.audio.GuildMusicManager;
import com.tzesh.tzebot.core.music.audio.PlayerManager;
import com.tzesh.tzebot.core.music.audio.TrackScheduler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * This is a simple class for listening voice updates of guilds to leave voice channels if there is no one in the channel
 *
 * @author tzesh
 */
public class VoiceUpdateListener extends AbstractEventListener<GuildVoiceUpdateEvent> {
    @Override
    protected void handle(GuildVoiceUpdateEvent event) {
        final Guild guild = event.getGuild();
        final AudioManager audioManager = guild.getAudioManager();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(guild);
        final AudioChannelUnion connectedChannel = audioManager.getConnectedChannel();

        if (connectedChannel == null || connectedChannel.getMembers().size() == 1) {
            flushQueueAndCloseAudioConnection(musicManager, audioManager);
        }
    }

    @Override
    protected boolean canHandle(GuildVoiceUpdateEvent event) {
        final AudioManager audioManager = event.getGuild().getAudioManager();

        return audioManager.isConnected();
    }

    private void flushQueueAndCloseAudioConnection(GuildMusicManager musicManager, AudioManager audioManager) {
        AudioPlayer player = musicManager.player;
        TrackScheduler scheduler = musicManager.scheduler;

        if (player.getPlayingTrack() != null) {
            scheduler.getQueue().clear();
            player.stopTrack();
            player.setPaused(false);
        }

        audioManager.closeAudioConnection();
    }
}

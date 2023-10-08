package com.tzesh.tzebot.core.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.tzesh.tzebot.core.music.constants.MusicCommonConstants;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tzesh.tzebot.core.music.constants.MusicCommonConstants.MAX_PLAYLIST_SIZE;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager(false);
        youtubeAudioSourceManager.setPlaylistPageCount(2);
        playerManager.registerSourceManager(youtubeAudioSourceManager);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String identifier, User user, boolean musicChannel, long guildID) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(musicManager, track);

                MessageEmbed messageEmbed = EmbedMessageBuilder.addedToNowPlaying(track.getInfo(), user, guildID);
                sendMessage(messageEmbed, channel, musicChannel);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                if (playlist.getTracks().size() > MAX_PLAYLIST_SIZE) {
                    MessageEmbed errorMessage = EmbedMessageBuilder.createErrorMessage("play.playlist.error.setTitle", "play.playlist.error.setDescription", user, guildID);
                    sendMessage(errorMessage, channel, musicChannel);
                    return;
                }

                play(musicManager, firstTrack);
                playlist.getTracks().remove(0);
                playlist.getTracks().forEach(musicManager.scheduler::queue);

                MessageEmbed successMessage = EmbedMessageBuilder.addedToQueue(playlist, user, guildID);
                sendMessage(successMessage, channel, musicChannel);
            }

            @Override
            public void noMatches() {
                MessageEmbed errorMessage = EmbedMessageBuilder.createErrorMessage("play.nothing.setTitle", "play.nothing.setDescription", user, guildID);
                sendMessage(errorMessage, channel, musicChannel);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                MessageEmbed errorMessage = EmbedMessageBuilder.createErrorMessage("play.error.setTitle", "play.error.setDescription", user, guildID);
                sendMessage(errorMessage, channel, musicChannel);
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    private void sendMessage(MessageEmbed message, TextChannel channel, boolean musicChannel) {
        if (musicChannel) {
            channel.sendMessage(MessageCreateData.fromEmbeds(message)).queue(sentMessage -> {
                sentMessage.delete().queueAfter(MusicCommonConstants.DEFAULT_DELAY, TimeUnit.SECONDS);
            });
        } else {
            channel.sendMessage(MessageCreateData.fromEmbeds(message)).queue();
        }
    }
}

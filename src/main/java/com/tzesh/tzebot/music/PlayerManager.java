package com.tzesh.tzebot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.Formatter.formatURL;

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

    public void loadAndPlay(TextChannel channel, String identifier, String name, String avatarURL, boolean musicChannel, long guildID) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                if (musicChannel) {
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setAuthor(track.getInfo().author);
                    success.setTitle(getMessage("general.icon.play", guildID) + getMessage("play.success.setTitle", guildID) + track.getInfo().title, track.getInfo().uri);
                    success.setFooter(getMessage("general.bythecommand", guildID) + name, avatarURL);
                    success.setImage(formatURL("https://img.youtube.com/vi/" + track.getInfo().uri, false) + "/0.jpg");
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });

                    play(musicManager, track);
                } else {
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setAuthor(track.getInfo().author);
                    success.setTitle(getMessage("general.icon.play", guildID) + getMessage("play.success.setTitle", guildID) + track.getInfo().title, track.getInfo().uri);
                    success.setFooter(getMessage("general.bythecommand", guildID) + name, avatarURL);
                    success.setImage(formatURL("https://img.youtube.com/vi/" + track.getInfo().uri, false) + "/0.jpg");
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();

                    play(musicManager, track);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                if (playlist.getTracks().size() > 200) {
                    if (musicChannel) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.playlist.error.setTitle", guildID));
                        error.setDescription(getMessage("play.playlist.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    } else {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.nothing.setTitle", guildID));
                        error.setDescription(getMessage("play.nothing.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                        return;
                    }
                }

                if (musicChannel) {

                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("play.playlist.setTitle1", guildID) + firstTrack.getInfo().title + getMessage("play.playlist.setTitle2", guildID) + playlist.getName() + ")");
                    success.setDescription(getMessage("play.playlist.size") + ": " + playlist.getTracks().size());
                    success.setFooter(getMessage("general.bythecommand", guildID) + name, avatarURL);
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });

                    play(musicManager, firstTrack);
                    playlist.getTracks().remove(0);

                    playlist.getTracks().forEach(musicManager.scheduler::queue);
                } else {
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("play.playlist.setTitle1", guildID) + firstTrack.getInfo().title + getMessage("play.playlist.setTitle2", guildID) + playlist.getName() + ")");
                    success.setFooter(getMessage("general.bythecommand", guildID) + name, avatarURL);
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });

                    play(musicManager, firstTrack);
                    playlist.getTracks().remove(0);

                    playlist.getTracks().forEach(musicManager.scheduler::queue);
                }
            }

            @Override
            public void noMatches() {
                if (musicChannel) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.nothing.setTitle", guildID));
                    error.setDescription(getMessage("play.nothing.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.nothing.setTitle", guildID));
                    error.setDescription(getMessage("play.nothing.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                }

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (musicChannel) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.error.setTitle", guildID));
                    error.setDescription(getMessage("play.error.setDescription", guildID) + exception.getMessage());
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.error.setTitle", guildID));
                    error.setDescription(getMessage("play.error.setDescription", guildID) + exception.getMessage());
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                }
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }
}

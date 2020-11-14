package TzeBot.music;

import TzeBot.essentials.Config;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static TzeBot.essentials.LanguageManager.getMessage;
import static TzeBot.utils.Formatter.formatTime;
import static TzeBot.utils.Formatter.formatURL;

public class MusicChannel {

    public static void handle(GuildMessageReactionAddEvent event) {
        final GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        final TextChannel channel = event.getChannel();
        final AudioManager audioManager = event.getGuild().getAudioManager();
        assert memberVoiceState != null;
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        final AudioPlayer player = musicManager.player;
        final TrackScheduler scheduler = musicManager.scheduler;
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        final long guildID = event.getGuild().getIdLong();
        final HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(guildID, (id) -> null);

        if (IDs.containsValue(event.getMessageIdLong())) {
            switch (event.getReactionEmote().getEmoji()) {
                case "⏯️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("pause.error.setTitle", guildID));
                        error.setDescription(getMessage("pause.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    if (!player.isPaused()) {
                        player.setPaused(true);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.pause", guildID) + getMessage("pause.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        player.setPaused(false);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.play", guildID) + getMessage("resume.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    }
                    break;
                case "⏹️":
                    if (audioManager.isConnected()) {
                        audioManager.closeAudioConnection();
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);

                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.leave", guildID) + getMessage("leave.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    }
                    if (!musicManager.player.isPaused()) {
                        musicManager.scheduler.getQueue().clear();
                        musicManager.player.stopTrack();
                        musicManager.player.setPaused(false);

                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("stop.error.setTitle", guildID));
                        error.setDescription(getMessage("stop.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    }
                    break;
                case "⏭️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("skip.error.setTitle", guildID));
                        error.setDescription(getMessage("skip.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.skip", guildID) + getMessage("skip.success.setTitle1", guildID) + player.getPlayingTrack().getInfo().title + getMessage("skip.success.setTitle2", guildID));
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });

                    scheduler.nextTrack();
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    break;
                case "\uD83D\uDD0A":
                    if (playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 25 > 100) {
                        playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(100);
                        Config.VOLUMES.put(event.getGuild().getIdLong(), 100);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 25);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 25);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    break;
                case "\uD83D\uDD09":
                    if (playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 25 < 0) {
                        playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(0);
                        Config.VOLUMES.put(event.getGuild().getIdLong(), 0);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 25);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 25);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    break;
                case "\uD83D\uDD01":
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        if (!scheduler.isRepeating()) {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                            scheduler.setRepeating(!scheduler.isRepeating());

                            success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.on.setTitle", guildID));
                            success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                            success.setTimestamp(Instant.now());

                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(3, TimeUnit.SECONDS);
                            });
                        } else {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                            scheduler.setRepeating(!scheduler.isRepeating());

                            success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.off.setTitle", guildID));
                            success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                            success.setTimestamp(Instant.now());

                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(3, TimeUnit.SECONDS);
                            });
                        }
                    }
                    break;
                case "\uD83D\uDCDC":
                    if (player.getPlayingTrack() != null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        AudioTrackInfo info = player.getPlayingTrack().getInfo();

                        EmbedBuilder nplaying = new EmbedBuilder();
                        nplaying.setTitle(info.title, info.uri);
                        nplaying.setAuthor(info.author);
                        nplaying.setImage(formatURL("https://img.youtube.com/vi/" + info.uri, false) + "/0.jpg");
                        nplaying.setDescription(String.format("%s %s - %s",
                                player.isPaused() ? "\u23F8" : "▶",
                                formatTime(player.getPlayingTrack().getPosition()),
                                formatTime(player.getPlayingTrack().getDuration())));
                        nplaying.setTimestamp(Instant.now());
                        nplaying.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        channel.sendMessage(nplaying.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    }
                    if (queue.isEmpty()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    int trackCount = Math.min(queue.size(), 20);
                    List<AudioTrack> tracks = new ArrayList<>(queue);
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(getMessage("general.icon.queue", guildID) + getMessage("queue.setTitle", guildID) + queue.size() + ")");

                    for (int i = 0; i < trackCount; i++) {
                        AudioTrack track = tracks.get(i);
                        AudioTrackInfo info = track.getInfo();

                        builder.appendDescription(String.format(
                                i + 1 + ") " + "%s - %s\n",
                                info.title,
                                info.author
                        ));
                    }
                    channel.sendMessage(builder.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    break;
                case "↪️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("nowplaying.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        scheduler.changePosition(15);
                    }
                    break;
                case "↩️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("nowplaying.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        scheduler.changePosition(-15);
                    }
                    break;
                case "\uD83D\uDD00":
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        scheduler.shufflePlaylist();
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.shuffle", guildID) + getMessage("shuffle.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
                        success.setTimestamp(Instant.now());

                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                    }
                    break;
                default:
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    break;
            }
        }
    }
}

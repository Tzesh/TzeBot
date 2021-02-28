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
            removeReaction(event);
            if (Math.random() > 0.85) voteUsMessage(event);
            if (!event.getMember().getVoiceState().inVoiceChannel()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.joinchannel.setTitle", guildID));
                error.setDescription(getMessage("join.joinchannel.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
                return;
            }
            if (event.getMember().getVoiceState().getChannel() != audioManager.getConnectedChannel()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("channel.same.setTitle", guildID));
                error.setDescription(getMessage("channel.same.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
                return;
            }
            switch (event.getReactionEmote().getEmoji()) {
                case "⏯️":
                    playOrPause(event, channel, player, guildID);
                    break;
                case "⏹️":
                    stopAndLeave(event, channel, audioManager, musicManager, player, guildID);
                    break;
                case "⏭️":
                    skip(event, channel, player, scheduler, guildID);
                    break;
                case "\uD83D\uDD0A":
                    volume(event, channel, playerManager, guildID, 25);
                    break;
                case "\uD83D\uDD09":
                    volume(event, channel, playerManager, guildID, -25);
                    break;
                case "\uD83D\uDD01":
                    loop(event, channel, musicManager, scheduler, guildID);
                    break;
                case "\uD83D\uDCDC":
                    queue(event, channel, player, queue, guildID);
                    break;
                case "↪️":
                    forwards(channel, player, scheduler, guildID, 15);
                    break;
                case "↩️":
                    forwards(channel, player, scheduler, guildID, -15);
                    break;
                case "\uD83D\uDD00":
                    shuffle(event, channel, musicManager, scheduler, guildID);
                    break;
            }
        }
    }

    private static void shuffle(GuildMessageReactionAddEvent event, TextChannel channel, GuildMusicManager musicManager, TrackScheduler scheduler, long guildID) {
        EmbedBuilder success;
        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {

            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
            error.setDescription(getMessage("loop.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue(message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        } else {

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
    }

    private static void forwards(TextChannel channel, AudioPlayer player, TrackScheduler scheduler, long guildID, int i) {
        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
            error.setDescription(getMessage("nowplaying.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue(message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        } else {

            scheduler.changePosition(i);
        }
    }

    private static void queue(GuildMessageReactionAddEvent event, TextChannel channel, AudioPlayer player, BlockingQueue<AudioTrack> queue, long guildID) {
        if (player.getPlayingTrack() != null) {
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
                message.delete().queueAfter(10, TimeUnit.SECONDS);
            });
        }
        if (queue.isEmpty()) {
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
            message.delete().queueAfter(10, TimeUnit.SECONDS);
        });
    }

    private static void loop(GuildMessageReactionAddEvent event, TextChannel channel, GuildMusicManager musicManager, TrackScheduler scheduler, long guildID) {
        EmbedBuilder success;
        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {

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
    }

    private static void volume(GuildMessageReactionAddEvent event, TextChannel channel, PlayerManager playerManager, long guildID, int volume) {
        EmbedBuilder success;
        if (volume > 0 ? playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + volume > 100 : playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + volume < 0) {
            playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(volume > 0 ? 100 : 0);
            Config.VOLUMES.put(event.getGuild().getIdLong(), volume > 0 ? 100 : 0);

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
        playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + volume);
        Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + volume);
        success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
        success.setTimestamp(Instant.now());

        channel.sendMessage(success.build()).queue(message -> {
            message.delete().queueAfter(3, TimeUnit.SECONDS);
        });
    }

    private static void skip(GuildMessageReactionAddEvent event, TextChannel channel, AudioPlayer player, TrackScheduler scheduler, long guildID) {
        if (player.getPlayingTrack() == null) {
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
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(getMessage("general.icon.skip", guildID) + getMessage("skip.success.setTitle1", guildID) + player.getPlayingTrack().getInfo().title + getMessage("skip.success.setTitle2", guildID));
        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
        success.setTimestamp(Instant.now());

        channel.sendMessage(success.build()).queue(message -> {
            message.delete().queueAfter(3, TimeUnit.SECONDS);
        });

        scheduler.nextTrack();
    }

    private static void stopAndLeave(GuildMessageReactionAddEvent event, TextChannel channel, AudioManager audioManager, GuildMusicManager musicManager, AudioPlayer player, long guildID) {
        if (player.getPlayingTrack() == null) {
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
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();

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

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
            success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(success.build()).queue(message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("stop.error.setTitle", guildID));
            error.setDescription(getMessage("stop.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue(message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        }
    }

    private static void playOrPause(GuildMessageReactionAddEvent event, TextChannel channel, AudioPlayer player, long guildID) {
        if (player.getPlayingTrack() == null) {
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

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.play", guildID) + getMessage("resume.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
            success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(success.build()).queue(message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        }
    }

    private static void removeReaction(GuildMessageReactionAddEvent event) {
        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
    }

    private static void voteUsMessage(GuildMessageReactionAddEvent event) {
        EmbedBuilder message = new EmbedBuilder();

        message.setColor(0xffffff);
        message.setTitle(getMessage("general.icon.hello", event.getGuild().getIdLong()) + " " + getMessage("general.notvoted", event.getGuild().getIdLong()) + event.getUser().getName(), "https://top.gg/bot/700416851678855168");
        message.setDescription(getMessage("general.notvoted.description", event.getGuild().getIdLong()));
        message.setFooter(getMessage("general.notvoted.footer", event.getGuild().getIdLong()));
        message.setTimestamp(Instant.now());

        event.getChannel().sendMessage(message.build()).queue(msg -> {
            msg.delete().queueAfter(10, TimeUnit.SECONDS);
        });
    }
}

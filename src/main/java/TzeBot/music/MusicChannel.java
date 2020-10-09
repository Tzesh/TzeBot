package TzeBot.music;

import TzeBot.essentials.Config;
import TzeBot.utils.Formatter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static TzeBot.essentials.LanguageDetector.getMessage;

public class MusicChannel {

    public static void handle(GuildMessageReactionAddEvent event) {
        final Member selfmember = event.getGuild().getSelfMember();
        final GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        final TextChannel channel = event.getChannel();
        final AudioManager audioManager = event.getGuild().getAudioManager();
        assert memberVoiceState != null;
        final VoiceChannel voiceChannel = memberVoiceState.getChannel();
        final VoiceChannel connectedVoiceChannel = audioManager.getConnectedChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        final AudioPlayer player = musicManager.player;
        final TrackScheduler scheduler = musicManager.scheduler;
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        final long guildID = event.getGuild().getIdLong();
        final HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(guildID, (id) -> null);

        if (IDs.containsValue(event.getMessageIdLong())) {
            switch (event.getReactionEmote().getEmoji()) {
                case "⏺️":
                    if (audioManager.isConnected()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.alreadyconnected.setTitle", guildID));
                        error.setDescription(getMessage("join.alreadyconnected.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    if (!memberVoiceState.inVoiceChannel()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.joinchannel.setTitle", guildID));
                        error.setDescription(getMessage("join.joinchannel.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
                        error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));

                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    audioManager.openAudioConnection(voiceChannel);
                    audioManager.setSelfDeafened(true);
                    int volume = Config.VOLUMES.computeIfAbsent(event.getGuild().getIdLong(), (id) -> 50);
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(volume);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.join", guildID) + getMessage("join.success.setTitle", guildID));
                    success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    success.clear();
                    break;
                case "⏏":
                    if (!audioManager.isConnected()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("leave.cannotleave.setTitle", guildID));
                        error.setDescription(getMessage("leave.notconnected", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    if (!connectedVoiceChannel.getMembers().contains(event.getMember())) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("leave.cannotleave.setTitle", guildID));
                        error.setDescription(getMessage("leave.notin", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    if (player.getPlayingTrack() != null) {
                        musicManager.scheduler.getQueue().clear();
                        musicManager.player.stopTrack();
                        musicManager.player.setPaused(false);

                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }

                    audioManager.closeAudioConnection();
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.leave", guildID) + getMessage("leave.success.setTitle", guildID));
                    success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    success.clear();
                    break;
                case "⏸":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("pause.error.setTitle", guildID));
                        error.setDescription(getMessage("pause.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        player.setPaused(true);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.pause", guildID) + getMessage("pause.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                    break;
                case "⏹️":
                    if (audioManager.isConnected()) {
                        audioManager.closeAudioConnection();
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);

                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.leave", guildID) + getMessage("leave.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                    if (!musicManager.player.isPaused()) {
                        musicManager.scheduler.getQueue().clear();
                        musicManager.player.stopTrack();
                        musicManager.player.setPaused(false);

                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("stop.error.setTitle", guildID));
                        error.setDescription(getMessage("stop.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    }
                    break;
                case "▶":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("resume.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else if (player.isPaused()) {
                        player.setPaused(false);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.play", guildID) + getMessage("resume.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("resume.nothingtoresumed.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    }
                    break;
                case "⏭️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("skip.error.setTitle", guildID));
                        error.setDescription(getMessage("skip.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.skip", guildID) + getMessage("skip.success.setTitle1", guildID) + player.getPlayingTrack().getInfo().title + getMessage("skip.success.setTitle2", guildID));
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    success.clear();

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


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 25);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 25);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    success.clear();
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


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 25);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 25);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    success.clear();
                    break;
                case "\uD83D\uDD01":
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        if (!scheduler.isRepeating()) {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                            scheduler.setRepeating(!scheduler.isRepeating());

                            success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.on.setTitle", guildID));
                            success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(3, TimeUnit.SECONDS);
                            });
                            success.clear();
                        } else {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                            scheduler.setRepeating(!scheduler.isRepeating());

                            success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.off.setTitle", guildID));
                            success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(3, TimeUnit.SECONDS);
                            });
                            success.clear();
                        }
                    }
                    break;
                case "\uD83D\uDCDC":
                    if (queue.isEmpty()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    int trackCount = Math.min(queue.size(), 20);
                    List<AudioTrack> tracks = new ArrayList<>(queue);
                    EmbedBuilder builder = EmbedUtils.defaultEmbed()
                            .setTitle(getMessage("general.icon.queue", guildID) + getMessage("queue.setTitle", guildID) + queue.size() + ")");

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
                case "⏯️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("nowplaying.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    AudioTrackInfo info = player.getPlayingTrack().getInfo();

                    channel.sendMessage(EmbedUtils.embedMessage(String.format("**" + getMessage("general.icon.nowplaying", guildID) + getMessage("nowplaying.nowplaying", guildID) + "** [%s]{%s}\n%s %s - %s",
                            info.title,
                            info.uri,
                            player.isPaused() ? "\u23F8" : "▶",
                            Formatter.formatTime(player.getPlayingTrack().getPosition()),
                            Formatter.formatTime(player.getPlayingTrack().getDuration())
                    )).build()).queue(message -> {
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


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    scheduler.changePosition(15);
                    break;
                case "↩️":
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(getMessage("nowplaying.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    scheduler.changePosition(-15);
                    break;
                case "\uD83D\uDD00":
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
                        error.setDescription(getMessage("loop.error.setDescription", guildID));


                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                        scheduler.shufflePlaylist();
                        success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(getMessage("general.icon.shuffle", guildID) + getMessage("shuffle.success.setTitle", guildID));
                        success.setFooter(getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());


                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(3, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                    break;
                default:
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queueAfter(3, TimeUnit.SECONDS);
                    break;
            }
        }
    }
}

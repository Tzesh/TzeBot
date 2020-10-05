package TzeBot.essentials;

import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class Listener extends ListenerAdapter {

    private static final long serialVersionUID = 1500;
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null && IDs.containsKey(event.getChannel().getIdLong())) {
            final long guildId = event.getGuild().getIdLong();
            manager.handle(event);
        } else {
            final long guildId = event.getGuild().getIdLong();
            String pre = Config.PREFIXES.computeIfAbsent(guildId, (id) -> Config.get("pre"));

            if (raw.startsWith(pre)) {
                manager.handle(event, pre);
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        final long guildID = event.getGuild().getIdLong();
        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            List<Long> roleIDs = Config.VOTEROLES.get(event.getMessageIdLong());
            switch (roleIDs.size()) {
                case 2:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    break;
                case 3:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.3", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    break;
                case 4:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.3", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.4", guildID))) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(3))).queue();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        
        final long guildID = event.getGuild().getIdLong();
        
        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null) {
            final TextChannel channel = event.getChannel();
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final PlayerManager playerManager = PlayerManager.getInstance();
            final GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
            final AudioPlayer player = musicManager.player;
            final TrackScheduler scheduler = musicManager.scheduler;
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

            if (IDs.containsValue(event.getMessageIdLong())) {
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.join", guildID))) {

                    if (audioManager.isConnected()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("join.alreadyconnected.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("join.alreadyconnected.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

                    if (!memberVoiceState.inVoiceChannel()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("join.joinchannel.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("join.joinchannel.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    VoiceChannel voiceChannel = memberVoiceState.getChannel();
                    Member selfmember = event.getGuild().getSelfMember();

                    if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("join.cannotjoin.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("join.cannotjoin.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    audioManager.openAudioConnection(voiceChannel);
                    int volume = Config.VOLUMES.computeIfAbsent(event.getGuild().getIdLong(), (id) -> 50);
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(volume);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    EmbedBuilder succes = new EmbedBuilder();
                    succes.setColor(0x00ff00);
                    succes.setTitle(LanguageDetector.getMessage("general.icon.join", guildID) + LanguageDetector.getMessage("join.success.setTitle", guildID));
                    succes.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(succes.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    succes.clear();
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.leave", guildID))) {

                    if (!audioManager.isConnected()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("leave.cannotleave.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("leave.notconnected", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    VoiceChannel voiceChannel = audioManager.getConnectedChannel();

                    if (!voiceChannel.getMembers().contains(event.getMember())) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("leave.cannotleave.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("leave.notin", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    if (player.getPlayingTrack() != null) {
                        musicManager.scheduler.getQueue().clear();
                        musicManager.player.stopTrack();
                        musicManager.player.setPaused(false);

                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.stop", guildID) + LanguageDetector.getMessage("stop.success.setTitle", guildID));
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }

                    audioManager.closeAudioConnection();
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.leave", guildID) + LanguageDetector.getMessage("leave.success.setTitle", guildID));
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    success.clear();
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.pause", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("pause.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("pause.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        player.setPaused(true);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.pause", guildID) + LanguageDetector.getMessage("pause.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.stop", guildID))) {
                    if (audioManager.isConnected()) {
                        audioManager.closeAudioConnection();
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();

                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.leave", guildID) + LanguageDetector.getMessage("leave.success.setTitle", guildID));
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                    if (!musicManager.player.isPaused()) {
                        musicManager.scheduler.getQueue().clear();
                        musicManager.player.stopTrack();
                        musicManager.player.setPaused(false);

                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.stop", guildID) + LanguageDetector.getMessage("stop.success.setTitle", guildID));
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("stop.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("stop.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    }
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.play", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("resume.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else if (player.isPaused()) {
                        player.setPaused(false);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.play", guildID) + LanguageDetector.getMessage("resume.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("resume.nothingtoresumed.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    }
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.skip", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("skip.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("skip.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.skip", guildID) + LanguageDetector.getMessage("skip.success.setTitle1", guildID) + player.getPlayingTrack().getInfo().title + LanguageDetector.getMessage("skip.success.setTitle2", guildID));
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    success.clear();

                    scheduler.nextTrack();
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.volume", guildID))) {
                    if (playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 10 > 100) {
                        playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(100);
                        Config.VOLUMES.put(event.getGuild().getIdLong(), 100);
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.volume", guildID) + LanguageDetector.getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 10);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + 10);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.volume", guildID) + LanguageDetector.getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    success.clear();
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.volumedown", guildID))) {
                    if (playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 10 < 0) {
                        playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(0);
                        Config.VOLUMES.put(event.getGuild().getIdLong(), 0);
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.volume", guildID) + LanguageDetector.getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                        return;
                    }
                    playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 10);
                    Config.VOLUMES.put(event.getGuild().getIdLong(), playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() - 10);
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.volume", guildID) + LanguageDetector.getMessage("volume.success.setTitle", guildID) + playerManager.getGuildMusicManager(event.getGuild()).player.getVolume() + "%");
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    success.clear();
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.loop", guildID))) {
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("loop.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("loop.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        if (scheduler.isRepeating() == false) {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                            scheduler.setRepeating(!scheduler.isRepeating());

                            EmbedBuilder success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(LanguageDetector.getMessage("general.icon.loop", guildID) + LanguageDetector.getMessage("loop.success.on.setTitle", guildID));
                            success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                            channel.sendTyping().queue();
                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                            success.clear();
                        } else {
                            event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                            scheduler.setRepeating(!scheduler.isRepeating());

                            EmbedBuilder success = new EmbedBuilder();
                            success.setColor(0x00ff00);
                            success.setTitle(LanguageDetector.getMessage("general.icon.loop", guildID) + LanguageDetector.getMessage("loop.success.off.setTitle", guildID));
                            success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                            channel.sendTyping().queue();
                            channel.sendMessage(success.build()).queue(message -> {
                                message.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                            success.clear();
                        }
                    }
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.queue", guildID))) {
                    if (queue.isEmpty()) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("loop.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("loop.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    int trackCount = Math.min(queue.size(), 20);
                    List<AudioTrack> tracks = new ArrayList<>(queue);
                    EmbedBuilder builder = EmbedUtils.defaultEmbed()
                            .setTitle(LanguageDetector.getMessage("general.icon.queue", guildID) + LanguageDetector.getMessage("queue.setTitle", guildID) + queue.size() + ")");

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
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.nowplaying", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("nowplaying.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    AudioTrackInfo info = player.getPlayingTrack().getInfo();

                    channel.sendMessage(EmbedUtils.embedMessage(String.format("**" + LanguageDetector.getMessage("general.icon.nowplaying", guildID) + LanguageDetector.getMessage("nowplaying.nowplaying", guildID) + "** [%s]{%s}\n%s %s - %s",
                            info.title,
                            info.uri,
                            player.isPaused() ? "\u23F8" : "▶",
                            formatTime(player.getPlayingTrack().getPosition()),
                            formatTime(player.getPlayingTrack().getDuration())
                    )).build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.next", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("nowplaying.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    scheduler.changePosition(15);
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.previous", guildID))) {
                    if (player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("nowplaying.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("nowplaying.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                    scheduler.changePosition(-15);
                }
                if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.shuffle", guildID))) {
                    if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("loop.error.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("loop.error.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                    } else {
                        event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                        scheduler.shufflePlaylist();
                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x00ff00);
                        success.setTitle(LanguageDetector.getMessage("general.icon.shuffle", guildID) + LanguageDetector.getMessage("shuffle.success.setTitle", guildID));
                        success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + event.getMember().getUser().getName(), event.getMember().getUser().getAvatarUrl());

                        channel.sendTyping().queue();
                        channel.sendMessage(success.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        success.clear();
                    }
                } else {
                    event.getChannel().removeReactionById(event.getMessageId(), event.getReactionEmote().getEmoji(), event.getUser()).queue();
                }
            }
        }

        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            List<Long> roleIDs = Config.VOTEROLES.computeIfAbsent(event.getMessageIdLong(), (id) -> null);
            if (roleIDs == null) {
                return;
            }
            switch (roleIDs.size()) {
                case 2:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    break;
                case 3:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.3", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    break;
                case 4:
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.1", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.2", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.3", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    if (event.getReactionEmote().getEmoji().equals(LanguageDetector.getMessage("general.icon.4", guildID))) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(3))).queue();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            Config.VOTEROLES.remove(event.getMessageIdLong());
        }
        if (Config.MUSICCHANNELS.containsKey(event.getMessageIdLong())) {
            Config.MUSICCHANNELS.remove(event.getGuild().getIdLong());
        }

        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null && IDs.containsValue(event.getMessageIdLong())) {
            Config.MUSICCHANNELS.remove(event.getGuild().getIdLong());
        }
    }

    private String formatTime(long timeInMilis) {
        final long hours = timeInMilis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMilis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMilis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

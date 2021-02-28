package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static TzeBot.essentials.LanguageManager.getMessage;
import static TzeBot.utils.Formatter.formatTime;
import static TzeBot.utils.Formatter.formatURL;

public class Play implements ICommand {

    private final YouTube youTube;

    public Play() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Tze Bot")
                    .build();

        } catch (Exception e) {
        }
        youTube = temp;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        String input = String.join(" ", ctx.getArgs());
        final GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        final VoiceChannel voiceChannel = memberVoiceState.getChannel();
        final Member selfmember = ctx.getGuild().getSelfMember();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final PlayerManager manager = PlayerManager.getInstance();
        final String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());
        boolean musicChannel = false;
        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> null);
        if (IDs != null) {
            musicChannel = IDs.containsKey(channel.getIdLong());
        }
        final long guildID = ctx.getGuild().getIdLong();

        if (musicChannel) {
            if (!isUrl(input)) {
                String ytSearched = searchYoutube(input);

                if (ytSearched == null) {
                    ctx.getMessage().delete().queue();
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.noresults.setTitle", guildID));
                    error.setDescription(getMessage("play.noresults.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue(message -> {
                        message.delete().queueAfter(3, TimeUnit.SECONDS);
                    });
                    return;
                }

                input = ytSearched;
            }

            if (!memberVoiceState.inVoiceChannel()) {
                ctx.getMessage().delete().queue();
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

            if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT) || !selfmember.hasPermission(voiceChannel, Permission.VOICE_SPEAK)) {
                ctx.getMessage().delete().queue();
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
                error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
                return;
            }

            if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                ctx.getMessage().delete().queue();
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
                error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
                return;
            }

            if (!audioManager.isConnected()) {
                audioManager.openAudioConnection(voiceChannel);
                audioManager.setSelfDeafened(true);
                int volume = Config.VOLUMES.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> 50);
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(volume);
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.join", guildID) + getMessage("join.success.setTitle", guildID));
                success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(success.build()).queue(message -> {
                    message.delete().queueAfter(3, TimeUnit.SECONDS);
                });
            }

            GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
            AudioPlayer player = musicManager.player;
            if (player.isPaused()) player.setPaused(false);
            if (player.getVolume() == 0) player.setVolume(50);
            manager.loadAndPlay(ctx.getChannel(), input, ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl(), true, guildID);
            ctx.getMessage().delete().queue();
        } else {
            if (!isUrl(input)) {
                String ytSearched = searchYoutube(input);

                if (ytSearched == null) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("play.noresults.setTitle", guildID));
                    error.setDescription(getMessage("play.noresults.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                    return;
                }

                input = ytSearched;
            }

            if (ctx.getArgs().isEmpty()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.403", guildID));
                error.setDescription(getMessage("play.noargs.setDescription1", guildID) + prefix + getMessage("play.noargs.setDescription2", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue();
                return;
            }

            if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT) || !selfmember.hasPermission(voiceChannel, Permission.VOICE_SPEAK)) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
                error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue();
                return;
            }

            if (!memberVoiceState.inVoiceChannel()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.joinchannel.setTitle", guildID));
                error.setDescription(getMessage("join.joinchannel.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue();
                return;
            }

            if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
                error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue();
                return;
            }

            if (!audioManager.isConnected()) {
                audioManager.openAudioConnection(voiceChannel);
                audioManager.setSelfDeafened(true);
                int volume = Config.VOLUMES.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> 50);
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(volume);
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.join", guildID) + getMessage("join.success.setTitle", guildID));
                success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(success.build()).queue();
            }
            GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
            AudioPlayer player = musicManager.player;
            if (player.isPaused()) player.setPaused(false);
            if (player.getVolume() == 0) player.setVolume(50);
            manager.loadAndPlay(ctx.getChannel(), input, ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl(), false, guildID);

            if (player.getPlayingTrack() != null) {
                AudioTrackInfo info = player.getPlayingTrack().getInfo();

                EmbedBuilder message = new EmbedBuilder();
                message.setAuthor(info.author);
                message.setTitle(info.title, info.uri);
                message.setImage(formatURL("https://img.youtube.com/vi/" + info.uri, false) + "/0.jpg");
                message.setDescription(String.format("%s %s - %s",
                        player.isPaused() ? "\u23F8" : "▶",
                        formatTime(player.getPlayingTrack().getPosition()),
                        formatTime(player.getPlayingTrack().getDuration())));
                message.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                message.setTimestamp(Instant.now());

                channel.sendMessage(message.build()).queue();
            }
        }
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list(Collections.singletonList("id,snippet"))
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType(Collections.singletonList("video"))
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("key"))
                    .execute()
                    .getItems();
            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();
                return "https://www.youtube.com/watch?v=" + videoId;
            }

        } catch (Exception e) {
            System.out.println("An error occured during the searching video in YouTube.");
        }
        return null;
    }

    @Override
    public String getName(long guildID) {
        return getMessage("play.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("play.gethelp1", guildID) + "\n"
                + getMessage("play.gethelp2", guildID) + Config.get("pre") + getName(guildID) + getMessage("play.gethelp3", guildID);
    }
}

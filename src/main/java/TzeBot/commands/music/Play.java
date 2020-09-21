package TzeBot.commands.music;

import TzeBot.essentials.Config;
import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> null);
        final long guildID = ctx.getGuild().getIdLong();

        if (IDs != null) {
            if (IDs.containsKey(channel.getIdLong())) {
                if (!isUrl(input)) {
                    String ytSearched = searchYoutube(input);

                    if (ytSearched == null) {
                        ctx.getMessage().delete().queue();
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("play.noresults.setTitle", guildID));
                        error.setDescription(LanguageDetector.getMessage("play.noresults.setDescription", guildID));

                        channel.sendTyping().queue();
                        channel.sendMessage(error.build()).queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        error.clear();
                        return;
                    }

                    input = ytSearched;
                }

                if (!memberVoiceState.inVoiceChannel()) {
                    ctx.getMessage().delete().queue();
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

                if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                    ctx.getMessage().delete().queue();
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

                if (!audioManager.isConnected()) {
                    audioManager.openAudioConnection(voiceChannel);
                    int volume = Config.VOLUMES.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> 50);
                    manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(volume);
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageDetector.getMessage("general.icon.join", guildID) + LanguageDetector.getMessage("join.success.setTitle", guildID));
                    success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                    channel.sendTyping().queue();
                    channel.sendMessage(success.build()).queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                    success.clear();
                }
                manager.loadAndPlay(ctx.getChannel(), input, ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl(), true, guildID);
                ctx.getMessage().delete().queue();
            }
        }
        if (IDs == null) {
            if (!isUrl(input)) {
                String ytSearched = searchYoutube(input);

                if (ytSearched == null) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("play.noresults.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("play.noresults.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                }

                input = ytSearched;
            }

            if (ctx.getArgs().isEmpty()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.403", guildID));
                error.setDescription(LanguageDetector.getMessage("play.noargs.setDescription1", guildID) + prefix + LanguageDetector.getMessage("play.noargs.setDescription2", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }

            if (!memberVoiceState.inVoiceChannel()) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("join.joinchannel.setTitle", guildID));
                error.setDescription(LanguageDetector.getMessage("join.joinchannel.setDescription", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }

            if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("join.cannotjoin.setTitle", guildID));
                error.setDescription(LanguageDetector.getMessage("join.cannotjoin.setDescription", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }

            if (!audioManager.isConnected()) {
                audioManager.openAudioConnection(voiceChannel);
                int volume = Config.VOLUMES.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> 50);
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(volume);
                EmbedBuilder succes = new EmbedBuilder();
                succes.setColor(0x00ff00);
                succes.setTitle(LanguageDetector.getMessage("general.icon.join", guildID) + LanguageDetector.getMessage("join.success.setTitle", guildID));
                succes.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                channel.sendTyping().queue();
                channel.sendMessage(succes.build()).queue();
                succes.clear();
            }

            GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
            AudioPlayer player = musicManager.player;

            manager.loadAndPlay(ctx.getChannel(), input, ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl(), false, guildID);

            AudioTrackInfo info = player.getPlayingTrack().getInfo();

            channel.sendMessage(EmbedUtils.embedMessage(String.format("**" + LanguageDetector.getMessage("general.icon.nowplaying", guildID) + LanguageDetector.getMessage("nowplaying.nowplaying", guildID) + "** [%s]{%s}\n%s %s - %s",
                    info.title,
                    info.uri,
                    player.isPaused() ? "\u23F8" : "▶",
                    formatTime(player.getPlayingTrack().getPosition()),
                    formatTime(player.getPlayingTrack().getDuration())
            )).build()).queue();
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
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("key"))
                    .execute()
                    .getItems();
            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();
                return "https://www.youtube.com/watch?v=" + videoId;
            }

        } catch (Exception e) {
        }

        return null;
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("play.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("play.gethelp1", guildID) + "\n"
                + LanguageDetector.getMessage("play.gethelp2", guildID) + Config.get("pre") + getName(guildID) + LanguageDetector.getMessage("play.gethelp3", guildID);
    }

    private String formatTime(long timeInMilis) {
        final long hours = timeInMilis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMilis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMilis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

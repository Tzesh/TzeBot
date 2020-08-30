package TzeBot.command.commands.music;

import TzeBot.Config;
import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayCommand implements ICommand {
    private final YouTube youTube;

    public PlayCommand () {
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

        TextChannel channel = ctx.getChannel();
        String input = String.join(" ", ctx.getArgs());
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfmember = ctx.getGuild().getSelfMember();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        PlayerManager manager = PlayerManager.getInstance();


        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("playcommand.noresults.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("playcommand.noresults.setDescription"));

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
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("playcommand.noargs.setDescription1") + Config.get("pre") + TzeBot.Languages.LanguageDetector.getMessage("playcommand.noargs.setDescription2"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("joincommand.joinchannel.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("joincommand.joinchannel.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("joincommand.cannotjoin.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("joincommand.cannotjoin.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(voiceChannel);
            manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(5);
            EmbedBuilder succes = new EmbedBuilder();
            succes.setColor(0x00ff00);
            succes.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("joincommand.success.setTitle"));
            succes.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(succes.build()).queue();
            succes.clear();
        }

        GuildMusicManager musicManager = manager.getGuildMusicManager(ctx.getGuild());
        AudioPlayer player = musicManager.player;

        manager.loadAndPlay(ctx.getChannel(), input, ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        channel.sendMessage(EmbedUtils.embedMessage(String.format(
                "**" + TzeBot.Languages.LanguageDetector.getMessage("nowplayingcommand.nowplaying") + "** [%s]{%s}\n%s %s - %s",
                info.title,
                info.uri,
                player.isPaused() ? "\u23F8" : "▶",
                formatTime(player.getPlayingTrack().getPosition()),
                formatTime(player.getPlayingTrack().getDuration())
        )).build()).queue();

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
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("playcommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("playcommand.gethelp1") + "\n" +
                TzeBot.Languages.LanguageDetector.getMessage("playcommand.gethelp2") + Config.get("pre") + getName() + TzeBot.Languages.LanguageDetector.getMessage("playcommand.gethelp3");
    }

    private String formatTime(long timeInMilis) {
        final long hours = timeInMilis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMilis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMilis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

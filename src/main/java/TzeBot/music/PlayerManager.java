package TzeBot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager (Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, String name, String avatarURL) {
        GuildMusicManager  musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.play") + TzeBot.essentials.LanguageDetector.getMessage("play.success.setTitle")  + track.getInfo().title);
                success.setDescription(track.getInfo().uri);
                success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + name, avatarURL);

                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
                
                play(musicManager, track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("play.playlist.setTitle1") + firstTrack.getInfo().title + TzeBot.essentials.LanguageDetector.getMessage("play.playlist.setTitle2") + playlist.getName() + ")");
                success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + name, avatarURL);
                
                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();

                play(musicManager, firstTrack);

                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("play.nothing.setTitle"));
                error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("play.nothing.setDescription") + trackUrl);

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("play.error.setTitle"));
                error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("play.error.setDescription") + exception.getMessage());

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}

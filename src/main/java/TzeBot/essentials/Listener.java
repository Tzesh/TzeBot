package TzeBot.essentials;

import TzeBot.music.GuildMusicManager;
import TzeBot.music.MusicChannel;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

import static TzeBot.essentials.LanguageManager.getMessage;
import static TzeBot.moderation.VoteRole.onReactionAdd;
import static TzeBot.moderation.VoteRole.onReactionRemove;

public class Listener extends ListenerAdapter {

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
            onReactionRemove(event);
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        
        HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(event.getGuild().getIdLong(), (id) -> null);
        if (IDs != null) {
            MusicChannel.handle(event);
        }

        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            List<Long> roleIDs = Config.VOTEROLES.computeIfAbsent(event.getMessageIdLong(), (id) -> null);
            if (roleIDs == null) {
                return;
            }
            onReactionAdd(event);
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

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        final AudioManager audioManager = event.getGuild().getAudioManager();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        final AudioPlayer player = musicManager.player;
        if (audioManager.isConnected() && audioManager.getConnectedChannel() == event.getChannelLeft() && audioManager.getConnectedChannel().getMembers().size() == 1) {
            if (player.getPlayingTrack() != null) {
                musicManager.scheduler.getQueue().clear();
                musicManager.player.stopTrack();
                musicManager.player.setPaused(false);
            }
            audioManager.closeAudioConnection();
        }
    }
}

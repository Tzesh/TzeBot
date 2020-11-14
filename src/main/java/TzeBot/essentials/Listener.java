package TzeBot.essentials;

import TzeBot.music.GuildMusicManager;
import TzeBot.music.MusicChannel;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;

import static TzeBot.essentials.LanguageManager.getMessage;
import static TzeBot.moderation.VoteRole.onReactionAdd;
import static TzeBot.moderation.VoteRole.onReactionRemove;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        for (Long guildID : Config.MUSICCHANNELS.keySet()) {
            HashMap<Long, Long> IDs = Config.MUSICCHANNELS.get(guildID);
            for (Long channelID : IDs.keySet()) {
                Long messageID = IDs.get(channelID);
                TextChannel channel = event.getJDA().getTextChannelById(channelID);
                EmbedBuilder success1 = new EmbedBuilder();
                success1.setColor(0xcccccc);
                success1.setTitle(getMessage("music.icon", guildID) + " " + getMessage("channel.setTitle", guildID));
                success1.setDescription(getMessage("channel.firstMessage", guildID));
                success1.addField(getMessage("general.icon.nowplaying", guildID), getMessage("channel.pauseresume", guildID), true);
                success1.addField(getMessage("general.icon.stop", guildID), getMessage("stop.gethelp", guildID), true);
                success1.addField(getMessage("general.icon.skip", guildID), getMessage("skip.gethelp", guildID), true);
                success1.addField(getMessage("general.icon.loop", guildID), getMessage("loop.gethelp", guildID), true);
                success1.addField(getMessage("general.icon.shuffle", guildID), getMessage("shuffle.gethelp", guildID), true);
                success1.addField(getMessage("general.icon.next", guildID), getMessage("channel.next", guildID), true);
                success1.addField(getMessage("general.icon.previous", guildID), getMessage("channel.previous", guildID), true);
                success1.addField(getMessage("general.icon.volumedown", guildID), getMessage("channel.volumedown", guildID), true);
                success1.addField(getMessage("general.icon.volume", guildID), getMessage("channel.volume", guildID), true);
                success1.addBlankField(true);
                success1.addField(getMessage("general.icon.queue", guildID), getMessage("channel.queuenp", guildID), true);
                success1.addBlankField(true);
                success1.setFooter(getMessage("channel.setFooter", guildID));
                success1.build();
                MessageEmbed messageEmbed = success1.build();
                if (channel != null) channel.editMessageById(messageID, messageEmbed).queue(message -> {
                    message.clearReactions().queue();
                    message.addReaction(getMessage("general.icon.nowplaying", guildID)).queue();
                    message.addReaction(getMessage("general.icon.stop", guildID)).queue();
                    message.addReaction(getMessage("general.icon.skip", guildID)).queue();
                    message.addReaction(getMessage("general.icon.loop", guildID)).queue();
                    message.addReaction(getMessage("general.icon.shuffle", guildID)).queue();
                    message.addReaction(getMessage("general.icon.next", guildID)).queue();
                    message.addReaction(getMessage("general.icon.previous", guildID)).queue();
                    message.addReaction(getMessage("general.icon.volumedown", guildID)).queue();
                    message.addReaction(getMessage("general.icon.volume", guildID)).queue();
                    message.addReaction(getMessage("general.icon.queue", guildID)).queue();
                });
            }
        }
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
        Config.VOTEROLES.remove(event.getMessageIdLong());
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

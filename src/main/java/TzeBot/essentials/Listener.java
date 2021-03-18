package TzeBot.essentials;

import TzeBot.music.GuildMusicManager;
import TzeBot.music.MusicChannel;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static TzeBot.essentials.LanguageManager.getMessage;
import static TzeBot.moderation.VoteRole.onReactionAdd;
import static TzeBot.moderation.VoteRole.onReactionRemove;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();
    private BotListManager botListManager;

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        if (Config.get("dbltoken").length() != 0) {
            botListManager = new BotListManager();
            botListManager.setStats(event.getJDA());
        }
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
                if (channel != null) {
                    channel.editMessageById(messageID, messageEmbed).queueAfter(1000, TimeUnit.MILLISECONDS, message -> {
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
        checkMembersAndLeave(event.getGuild(), event.getChannelLeft(), event);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        checkMembersAndLeave(event.getGuild(), event.getChannelLeft(), event);
    }

    private void checkMembersAndLeave(Guild guild, VoiceChannel channelLeft, @Nonnull Event event) {
        final AudioManager audioManager = guild.getAudioManager();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(guild);
        final AudioPlayer player = musicManager.player;
        if (!audioManager.isConnected()) return;
        if (audioManager.getConnectedChannel() == channelLeft && channelLeft.getMembers().size() == 1) {
            if (player.getPlayingTrack() != null) {
                musicManager.scheduler.getQueue().clear();
                musicManager.player.stopTrack();
                musicManager.player.setPaused(false);
            }
            audioManager.closeAudioConnection();
        }
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        if (this.botListManager != null)
        botListManager.setStats(event.getJDA());
        TextChannel defaultChannel = event.getGuild().getDefaultChannel();
        if (defaultChannel != null && event.getGuild().getSelfMember().hasPermission(defaultChannel, Permission.MESSAGE_WRITE))
            event.getGuild().getDefaultChannel().sendMessage("Greetings " + defaultChannel.getGuild().getName() + "," +
                    "\n\uD83D\uDD33️ You can look at all categories of the commands of TzeBot by just typing `.help`" +
                    "\n\uD83D\uDD33️ You can setup music channel which allows you to play songs without `.play` and use player with reactions (emotes)" +
                    "\n\uD83D\uDD33️ Besides you can change language to Turkish by just typing: `.language Turkish`" +
                    "\n\uD83D\uDD33️ Feel free to join our support channel if you encounter any kind of problems" +
                    "\n\uD83D\uDD33️ You can always kick TzeBot to reset every single preference and data about your server" +
                    "\n\uD83D\uDD33️ Thank you for choosing TzeBot").queue();
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        long guildID = event.getGuild().getIdLong();
        Config.PREFIXES.remove(guildID);
        Config.CHANNELS.remove(guildID);
        Config.LANGUAGES.remove(guildID);
        Config.CHANNELCREATED.remove(guildID);
        Config.MUSICCHANNELS.remove(guildID);
        Config.VOTEROLES.remove(guildID);
        Config.VOLUMES.remove(guildID);
    }
}

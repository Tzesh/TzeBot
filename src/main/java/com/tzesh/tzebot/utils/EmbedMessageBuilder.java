package com.tzesh.tzebot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.music.enums.MusicEmoteUnicodes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.tzesh.tzebot.core.language.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.FormatHelper.formatTime;
import static com.tzesh.tzebot.utils.FormatHelper.formatURL;

/**
 * This is a simple embed message builder class
 *
 * @author tzesh
 */
public class EmbedMessageBuilder {
    private static final int red = 0xff0000;
    private static final int gray = 0xcccccc;
    private static final int green = 0x00ff00;

    public static MessageEmbed createSuccessMessage(String titleKey, String descriptionKey, String iconKey, User user, GuildChannel guildChannel) {
        return createCustomMessage(green, titleKey, descriptionKey, iconKey, user, guildChannel);
    }

    public static MessageEmbed createSuccessMessage(String titleKey, String descriptionKey, User user, GuildChannel guildChannel) {
        return createCustomMessage(red, titleKey, descriptionKey, "general.icon.success", user, guildChannel);
    }

    public static MessageEmbed createErrorMessage(String titleKey, String descriptionKey, User user, GuildChannel guildChannel) {
        return createCustomMessage(red, titleKey, descriptionKey, "general.icon.error", user, guildChannel);
    }

    public static MessageEmbed createCustomSuccessMessage(String title, String description, User user, GuildChannel guildChannel) {
        return createCustomMessageWithoutReadyMessage(green, title, description, "general.icon.success", user, guildChannel);
    }

    public static MessageEmbed createCustomMessage(int color, String titleKey, String descriptionKey, String iconKey, User user, GuildChannel guildChannel) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setTitle(getMessage(iconKey, guildChannel) + getMessage(titleKey, guildChannel))
                .setDescription(getMessage(descriptionKey, guildChannel))
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl())
                .setTimestamp(Instant.now());

        return builder.build();
    }

    public static MessageEmbed createCustomMessageWithoutReadyMessage(int color, String title, String description, String iconKey, User user, GuildChannel guildChannel) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setTitle(getMessage(iconKey, guildChannel) + title)
                .setDescription(description)
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl())
                .setTimestamp(Instant.now());

        return builder.build();
    }

    public static MessageEmbed createMusicChannelEmbeddedMessage(GuildChannel guildChannel) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(gray)
                .setTitle(getMessage("music.icon", guildChannel) + " " + getMessage("channel.setTitle", guildChannel))
                .setDescription(getMessage("channel.firstMessage", guildChannel))
                .addField(getMessage("general.icon.nowplaying", guildChannel), getMessage("channel.pauseresume", guildChannel), true)
                .addField(getMessage("general.icon.stop", guildChannel), getMessage("stop.gethelp", guildChannel), true)
                .addField(getMessage("general.icon.skip", guildChannel), getMessage("skip.gethelp", guildChannel), true)
                .addField(getMessage("general.icon.loop", guildChannel), getMessage("loop.gethelp", guildChannel), true)
                .addField(getMessage("general.icon.shuffle", guildChannel), getMessage("shuffle.gethelp", guildChannel), true)
                .addField(getMessage("general.icon.next", guildChannel), getMessage("channel.next", guildChannel), true)
                .addField(getMessage("general.icon.previous", guildChannel), getMessage("channel.previous", guildChannel), true)
                .addField(getMessage("general.icon.volumedown", guildChannel), getMessage("channel.volumedown", guildChannel), true)
                .addField(getMessage("general.icon.volume", guildChannel), getMessage("channel.volume", guildChannel), true)
                .addBlankField(true)
                .addField(getMessage("general.icon.queue", guildChannel), getMessage("channel.queuenp", guildChannel), true)
                .addBlankField(true)
                .setFooter(getMessage("channel.setFooter", guildChannel));
        return builder.build();
    }

    public static MessageEmbed createNowPlayingEmbeddedMessage(AudioPlayer player, User user, GuildChannel guildChannel) {
        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(info.title, info.uri)
                .setAuthor(info.author)
                .setImage(formatURL("https://img.youtube.com/vi/" + info.uri, false) + "/0.jpg")
                .setDescription(String.format("%s %s - %s",
                        player.isPaused() ? "\u23F8" : "▶",
                        formatTime(player.getPlayingTrack().getPosition()),
                        formatTime(player.getPlayingTrack().getDuration())))
                .setTimestamp(Instant.now())
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl());

        return builder.build();
    }

    public static MessageEmbed createQueueEmbeddedMessage(BlockingQueue<AudioTrack> queue, User user, GuildChannel guildChannel) {
        int trackCount = queue.size();
        List<AudioTrack> tracks = new ArrayList<>(queue);

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(getMessage("general.icon.queue", guildChannel) + getMessage("queue.setTitle", guildChannel) + queue.size() + ")")
                .setTimestamp(Instant.now())
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl());

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    i + 1 + ") " + "%s - %s\n",
                    info.title,
                    info.author
            ));
        }

        return builder.build();
    }

    public static MessageEmbed addedToNowPlaying(AudioTrackInfo info, User user, GuildChannel guildChannel) {
        EmbedBuilder success = new EmbedBuilder()
                .setColor(0x00ff00)
                .setAuthor(info.author)
                .setTitle(getMessage("general.icon.play", guildChannel) + getMessage("play.success.setTitle", guildChannel) + info.title, info.uri)
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl())
                .setImage(formatURL("https://img.youtube.com/vi/" + info.uri, false) + "/0.jpg")
                .setTimestamp(Instant.now());

        return success.build();
    }

    public static MessageEmbed addedToQueue(AudioPlaylist audioPlaylist, User user, GuildChannel guildChannel) {
        AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
        EmbedBuilder success = new EmbedBuilder()
                .setColor(0x00ff00)
                .setTitle(getMessage("play.playlist.setTitle1", guildChannel) + firstTrack.getInfo().title + getMessage("play.playlist.setTitle2", guildChannel) + audioPlaylist.getName() + ")")
                .setDescription(getMessage("play.playlist.size") + ": " + audioPlaylist.getTracks().size())
                .setFooter(getMessage("general.bythecommand", guildChannel) + user.getName(), user.getAvatarUrl())
                .setTimestamp(Instant.now());

        return success.build();
    }

    public static void initializeMusicChannelEmojiControls(Message message) {
        message.addReaction(MusicEmoteUnicodes.nowPlaying.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.stop.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.skip.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.loop.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.shuffle.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.next.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.previous.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.volumedown.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.volumeup.getUnicode()).queue();
        message.addReaction(MusicEmoteUnicodes.queue.getUnicode()).queue();
    }
}

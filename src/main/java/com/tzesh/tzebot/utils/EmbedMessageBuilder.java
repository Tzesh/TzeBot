package com.tzesh.tzebot.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.tzesh.tzebot.core.music.enums.MusicEmoteUnicodes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.tzesh.tzebot.core.LanguageManager.getMessage;
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

    public static MessageEmbed createSuccessMessage(String titleKey, String descriptionKey, String iconKey, User user, long guildID) {
        return createCustomMessage(green, titleKey, descriptionKey, iconKey, user, guildID);
    }

    public static MessageEmbed createSuccessMessage(String titleKey, String descriptionKey, User user, long guildID) {
        return createCustomMessage(red, titleKey, descriptionKey, "general.icon.success", user, guildID);
    }

    public static MessageEmbed createErrorMessage(String titleKey, String descriptionKey, User user, long guildID) {
        return createCustomMessage(red, titleKey, descriptionKey, "general.icon.error", user, guildID);
    }

    public static MessageEmbed createCustomSuccessMessage(String title, String description, User user, long guildID) {
        return createCustomMessageWithoutReadyMessage(green, title, description, "general.icon.success", user, guildID);
    }

    public static MessageEmbed createCustomMessage(int color, String titleKey, String descriptionKey, String iconKey, User user, long guildID) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setTitle(getMessage(iconKey, guildID) + getMessage(titleKey, guildID))
                .setDescription(getMessage(descriptionKey, guildID))
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl())
                .setTimestamp(Instant.now());

        return builder.build();
    }

    public static MessageEmbed createCustomMessageWithoutReadyMessage(int color, String title, String description, String iconKey, User user, long guildID) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setTitle(getMessage(iconKey, guildID) + title)
                .setDescription(description)
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl())
                .setTimestamp(Instant.now());

        return builder.build();
    }

    public static MessageEmbed createMusicChannelEmbeddedMessage(long guildID) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(gray)
                .setTitle(getMessage("music.icon", guildID) + " " + getMessage("channel.setTitle", guildID))
                .setDescription(getMessage("channel.firstMessage", guildID))
                .addField(getMessage("general.icon.nowplaying", guildID), getMessage("channel.pauseresume", guildID), true)
                .addField(getMessage("general.icon.stop", guildID), getMessage("stop.gethelp", guildID), true)
                .addField(getMessage("general.icon.skip", guildID), getMessage("skip.gethelp", guildID), true)
                .addField(getMessage("general.icon.loop", guildID), getMessage("loop.gethelp", guildID), true)
                .addField(getMessage("general.icon.shuffle", guildID), getMessage("shuffle.gethelp", guildID), true)
                .addField(getMessage("general.icon.next", guildID), getMessage("channel.next", guildID), true)
                .addField(getMessage("general.icon.previous", guildID), getMessage("channel.previous", guildID), true)
                .addField(getMessage("general.icon.volumedown", guildID), getMessage("channel.volumedown", guildID), true)
                .addField(getMessage("general.icon.volume", guildID), getMessage("channel.volume", guildID), true)
                .addBlankField(true)
                .addField(getMessage("general.icon.queue", guildID), getMessage("channel.queuenp", guildID), true)
                .addBlankField(true)
                .setFooter(getMessage("channel.setFooter", guildID));
        return builder.build();
    }

    public static MessageEmbed createNowPlayingEmbeddedMessage(AudioPlayer player, User user, long guildID) {
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
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl());

        return builder.build();
    }

    public static MessageEmbed createQueueEmbeddedMessage(BlockingQueue<AudioTrack> queue, User user, long guildID) {
        int trackCount = queue.size();
        List<AudioTrack> tracks = new ArrayList<>(queue);

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(getMessage("general.icon.queue", guildID) + getMessage("queue.setTitle", guildID) + queue.size() + ")")
                .setTimestamp(Instant.now())
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl());

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

    public static MessageEmbed addedToNowPlaying(AudioTrackInfo info, User user, long guildID) {
        EmbedBuilder success = new EmbedBuilder()
                .setColor(0x00ff00)
                .setAuthor(info.author)
                .setTitle(getMessage("general.icon.play", guildID) + getMessage("play.success.setTitle", guildID) + info.title, info.uri)
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl())
                .setImage(formatURL("https://img.youtube.com/vi/" + info.uri, false) + "/0.jpg")
                .setTimestamp(Instant.now());

        return success.build();
    }

    public static MessageEmbed addedToQueue(AudioPlaylist audioPlaylist, User user, long guildID) {
        AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
        EmbedBuilder success = new EmbedBuilder()
                .setColor(0x00ff00)
                .setTitle(getMessage("play.playlist.setTitle1", guildID) + firstTrack.getInfo().title + getMessage("play.playlist.setTitle2", guildID) + audioPlaylist.getName() + ")")
                .setDescription(getMessage("play.playlist.size") + ": " + audioPlaylist.getTracks().size())
                .setFooter(getMessage("general.bythecommand", guildID) + user.getName(), user.getAvatarUrl())
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

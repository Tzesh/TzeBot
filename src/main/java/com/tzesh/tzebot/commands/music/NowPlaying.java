package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.music.GuildMusicManager;
import com.tzesh.tzebot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.Formatter.formatTime;
import static com.tzesh.tzebot.utils.Formatter.formatURL;

public class NowPlaying implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;
        final long guildID = ctx.getGuild().getIdLong();

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
            error.setDescription(getMessage("nowplaying.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

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

        channel.sendMessage(MessageCreateData.fromEmbeds(message.build())).queue();
    }

    @Override
    public String getName(long guildID) {
        return getMessage("nowplaying.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("nowplaying.gethelp", guildID);
    }
}

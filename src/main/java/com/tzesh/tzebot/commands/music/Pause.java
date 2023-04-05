package com.tzesh.tzebot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.music.GuildMusicManager;
import com.tzesh.tzebot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;

public class Pause implements ICommand {

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
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("pause.error.setTitle", guildID));
            error.setDescription(getMessage("pause.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            error.clear();
        } else {
            player.setPaused(true);
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.pause", guildID) + getMessage("pause.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
            success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("pause.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("pause.gethelp", guildID);
    }

}

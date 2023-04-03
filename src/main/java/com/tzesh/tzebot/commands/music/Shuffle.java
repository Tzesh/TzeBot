/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.music.GuildMusicManager;
import com.tzesh.tzebot.music.PlayerManager;
import com.tzesh.tzebot.music.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;


/**
 * @author Tzesh
 */
public class Shuffle implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final TrackScheduler scheduler = musicManager.scheduler;
        final long guildID = ctx.getGuild().getIdLong();

        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
            error.setDescription(getMessage("loop.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            scheduler.shufflePlaylist();
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.shuffle", guildID) + getMessage("shuffle.success.setTitle", guildID));
            success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("shuffle.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("shuffle.gethelp", guildID);
    }

}

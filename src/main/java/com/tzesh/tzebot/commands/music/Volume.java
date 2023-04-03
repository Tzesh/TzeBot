package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.Config;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.Controller.isInteger;


public class Volume implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager manager = PlayerManager.getInstance();
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        final String input = String.join(" ", ctx.getArgs());
        if (isInteger(input)) {
            if (Integer.parseInt(input) > 100 || Integer.parseInt(input) < 0) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("volume.error.setTitle", guildID));
                error.setDescription(getMessage("volume.error.setDescription", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
                error.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            } else {
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
                Config.VOLUMES.put(ctx.getGuild().getIdLong(), Integer.parseInt(input));
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.volume", guildID) + getMessage("volume.success.setTitle", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
                success.setFooter(getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
            }
        } else {
            if (input.equals("")) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("volume.error.setTitle", guildID));
                error.setDescription(getMessage("volume.error.setDescription", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
                error.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                return;
            }
            if (ctx.getArgs().size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.403", guildID));
                error.setDescription(getMessage("play.noargs.setDescription1", guildID) + Config.get("pre") + getMessage("play.noargs.setDescription2", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("volume.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("volume.gethelp", guildID);
    }

}

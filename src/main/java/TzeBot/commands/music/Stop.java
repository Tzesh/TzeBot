package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Instant;

import static TzeBot.essentials.LanguageManager.getMessage;


public class Stop implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final long guildID = ctx.getGuild().getIdLong();

        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.leave", guildID) + getMessage("leave.success.setTitle", guildID));
            success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(success.build()).queue();
        }
        if (!musicManager.player.isPaused()) {
            musicManager.scheduler.getQueue().clear();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
            success.setFooter(getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            success.setTimestamp(Instant.now());

            channel.sendMessage(success.build()).queue();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("stop.error.setTitle", guildID));
            error.setDescription(getMessage("stop.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
        }

    }

    @Override
    public String getName(long guildID) {
        return getMessage("stop.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("stop.gethelp", guildID);
    }
}

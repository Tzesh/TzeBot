package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;


import static TzeBot.essentials.LanguageDetector.getMessage;

public class Leave implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;
        final long guildID = ctx.getGuild().getIdLong();

        if (!audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("leave.cannotleave.setTitle", guildID));
            error.setDescription(getMessage("leave.notconnected", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("leave.cannotleave.setTitle", guildID));
            error.setDescription(getMessage("leave.notin", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        if (player.getPlayingTrack() != null) {
            musicManager.scheduler.getQueue().clear();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.stop", guildID) + getMessage("stop.success.setTitle", guildID));
            success.setFooter(getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            
            channel.sendMessage(success.build()).queue();
            success.clear();
        }

        audioManager.closeAudioConnection();
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(getMessage("general.icon.leave", guildID) + getMessage("leave.success.setTitle", guildID));
        success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        
        channel.sendMessage(success.build()).queue();
        success.clear();

    }

    @Override
    public String getName(long guildID) {
        return getMessage("leave.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("leave.gethelp", guildID);
    }
}

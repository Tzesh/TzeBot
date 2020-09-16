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

public class Leave implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;

        if (!audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("leave.cannotleave.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("leave.notconnected"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("leave.cannotleave.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("leave.notin"));

            channel.sendTyping().queue();
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
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.stop") + TzeBot.essentials.LanguageDetector.getMessage("stop.success.setTitle"));
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
        }
        
        

        audioManager.closeAudioConnection();
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.leave") + TzeBot.essentials.LanguageDetector.getMessage("leave.success.setTitle"));
        success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();

    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("leave.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("leave.gethelp");
    }
}

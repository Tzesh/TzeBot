package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

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
            success.setTitle(LanguageDetector.getMessage("general.icon.leave", guildID) + LanguageDetector.getMessage("leave.success.setTitle", guildID));
            success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
        }
        if (!musicManager.player.isPaused()) {
            musicManager.scheduler.getQueue().clear();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(LanguageDetector.getMessage("general.icon.stop", guildID) + LanguageDetector.getMessage("stop.success.setTitle", guildID));
            success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("stop.error.setTitle", guildID));
            error.setDescription(LanguageDetector.getMessage("stop.error.setDescription", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }

    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("stop.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("stop.gethelp", guildID);
    }
}

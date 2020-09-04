package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Loop implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        TrackScheduler scheduler = musicManager.scheduler;

        

        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("loop.error.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("loop.error.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            scheduler.setRepeating(!scheduler.isRepeating());

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.loop") + TzeBot.essentials.LanguageDetector.getMessage("loop.success.setTitle"));
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
            }
        }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("loop.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("loop.gethelp");
    }
}

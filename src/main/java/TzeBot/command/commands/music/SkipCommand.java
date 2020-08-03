package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Nothing is playing at this time.");
            error.setDescription("Cannot skip the unplayed song.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle("✅ Current song `" + player.getPlayingTrack().getInfo().title + "` is skipped.");
        succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        scheduler.nextTrack();

        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current song.";
    }
}

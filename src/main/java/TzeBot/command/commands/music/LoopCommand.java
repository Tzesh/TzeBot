package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LoopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ The queue is empty.");
            error.setDescription("You might wanna add some songs into it.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            List<AudioTrack> tracks = new ArrayList<>(queue);
            musicManager.scheduler.queue(musicManager.player.getPlayingTrack()); // adding the current playing song in the first place.
            for (AudioTrack audioTrack : tracks) {
                musicManager.scheduler.queue(audioTrack);
            }

            EmbedBuilder succes = new EmbedBuilder();
            succes.setColor(0x00ff00);
            succes.setTitle("✅ Looped the current queue once.");
            succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


            channel.sendTyping().queue();
            channel.sendMessage(succes.build()).queue();
            succes.clear();

            }
        }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String getHelp() {
        return "Loops the current queue.";
    }
}

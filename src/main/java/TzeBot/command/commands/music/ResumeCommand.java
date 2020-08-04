package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ There is nothing.");
            error.setDescription("There is nothing to be resumed.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();

        } else if (player.isPaused()) {
            player.setPaused(false);
            EmbedBuilder succes = new EmbedBuilder();
            succes.setColor(0x00ff00);
            succes.setTitle("✅ Resumed the current song : " + player.getPlayingTrack().getInfo().title);
            succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


            channel.sendTyping().queue();
            channel.sendMessage(succes.build()).queue();
            succes.clear();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ There is nothing to be resumed.");
            error.setDescription("You have to pause the current song first.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Resumes the current song.";
    }
}

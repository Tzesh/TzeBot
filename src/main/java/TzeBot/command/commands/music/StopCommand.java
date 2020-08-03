package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class StopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        if (!musicManager.player.isPaused()) {
            musicManager.scheduler.getQueue().clear();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);

            EmbedBuilder succes = new EmbedBuilder();
            succes.setColor(0x00ff00);
            succes.setTitle("✅ Stopped and cleared the queue.");
            succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


            ctx.getChannel().sendTyping().queue();
            ctx.getChannel().sendMessage(succes.build()).queue();
            succes.clear();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Nothing is playing at this time.");
            error.setDescription("Cannot stop the song and clear the queue.");

            ctx.getChannel().sendTyping().queue();
            ctx.getChannel().sendMessage(error.build()).queue();
            error.clear();
        }



    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stops the music player and clears the queue.";
    }
}

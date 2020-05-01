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

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        EmbedBuilder succces = new EmbedBuilder();
        succces.setColor(0x00ff00);
        succces.setTitle("✅ Stopped and cleared the queue.");

        ctx.getChannel().sendTyping().queue();
        ctx.getChannel().sendMessage(succces.build()).queue();
        succces.clear();

    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stops the music player.";
    }
}

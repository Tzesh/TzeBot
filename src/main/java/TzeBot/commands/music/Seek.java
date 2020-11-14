package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageManager;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;
import java.util.List;

import static TzeBot.essentials.LanguageManager.getMessage;

public class Seek implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;
        final TrackScheduler scheduler = musicManager.scheduler;
        final List<String> args = ctx.getArgs();

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
            error.setDescription(getMessage("nowplaying.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }
        if (args.size() != 1) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("seek.error.setTitle", guildID));
            error.setDescription(LanguageManager.getMessage("seek.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
        } else {
            if (args.get(0).equals(getMessage("seek.forward", guildID))) {
                scheduler.changePosition(15);
                ctx.getMessage().addReaction("\uD83D\uDC4D").queue();
            } else if (args.get(0).equals(getMessage("seek.backward", guildID))) {
                scheduler.changePosition(-15);
                ctx.getMessage().addReaction("\uD83D\uDC4D").queue();
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("seek.error.setTitle", guildID));
                error.setDescription(LanguageManager.getMessage("seek.error.setDescription", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(error.build()).queue();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("seek.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("seek.gethelp", guildID).replace(".", Config.PREFIXES.get(guildID));
    }
}

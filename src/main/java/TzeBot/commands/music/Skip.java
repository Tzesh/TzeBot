package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static TzeBot.essentials.LanguageManager.getMessage;

public class Skip implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final TrackScheduler scheduler = musicManager.scheduler;
        final AudioPlayer player = musicManager.player;
        final long guildID = ctx.getGuild().getIdLong();

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("skip.error.setTitle", guildID));
            error.setDescription(getMessage("skip.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(getMessage("general.icon.skip", guildID) + getMessage("skip.success.setTitle1", guildID) + player.getPlayingTrack().getInfo().title + getMessage("skip.success.setTitle2", guildID));
        success.setFooter(getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
        success.setTimestamp(Instant.now());

        scheduler.nextTrack();
        channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
    }

    @Override
    public String getName(long guildID) {
        return getMessage("skip.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("skip.gethelp", guildID);
    }
}

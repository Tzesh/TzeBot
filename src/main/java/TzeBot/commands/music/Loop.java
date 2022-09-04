package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static TzeBot.essentials.LanguageManager.getMessage;

public class Loop implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final TrackScheduler scheduler = musicManager.scheduler;
        final long guildID = ctx.getGuild().getIdLong();

        if (musicManager.scheduler.getQueue().isEmpty() && musicManager.player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("loop.error.setTitle", guildID));
            error.setDescription(getMessage("loop.error.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            if (scheduler.isRepeating() == false) {
                scheduler.setRepeating(!scheduler.isRepeating());

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.on.setTitle", guildID));
                success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
            } else {
                scheduler.setRepeating(!scheduler.isRepeating());

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.loop", guildID) + getMessage("loop.success.off.setTitle", guildID));
                success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("loop.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("loop.gethelp", guildID);
    }
}

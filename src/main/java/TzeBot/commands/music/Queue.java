package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Queue implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
        final long guildID = ctx.getGuild().getIdLong();

        if (queue.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("loop.error.setTitle", guildID));
            error.setDescription(LanguageDetector.getMessage("loop.error.setDescription", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle(LanguageDetector.getMessage("general.icon.queue", guildID) + LanguageDetector.getMessage("queue.setTitle", guildID) + queue.size() + ")");

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    i + 1 + ") " + "%s - %s\n",
                    info.title,
                    info.author
            ));
        }
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("queue.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("queue.gethelp", guildID);
    }
}

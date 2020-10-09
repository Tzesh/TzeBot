package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import static TzeBot.essentials.LanguageDetector.getMessage;


public class Resume implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;
        final long guildID = ctx.getGuild().getIdLong();

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
            error.setDescription(getMessage("resume.error.setDescription", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else if (player.isPaused()) {
            player.setPaused(false);
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(getMessage("general.icon.play", guildID) + getMessage("resume.success.setTitle", guildID) + player.getPlayingTrack().getInfo().title);
            success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            
            channel.sendMessage(success.build()).queue();
            success.clear();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("nowplaying.error.setTitle", guildID));
            error.setDescription(getMessage("resume.nothingtoresumed.setDescription", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("resume.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("resume.gethelp", guildID);
    }
}

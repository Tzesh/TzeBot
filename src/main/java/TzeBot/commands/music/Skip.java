package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import TzeBot.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Skip implements ICommand {
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
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("skip.error.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("skip.error.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.skip") + TzeBot.essentials.LanguageDetector.getMessage("skip.success.setTitle1") + player.getPlayingTrack().getInfo().title + TzeBot.essentials.LanguageDetector.getMessage("skip.success.setTitle2"));
        success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        scheduler.nextTrack();

        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("skip.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("skip.gethelp");
    }
}

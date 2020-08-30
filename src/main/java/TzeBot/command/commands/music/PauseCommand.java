package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("pausecommand.error.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("pausecommand.error.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            player.setPaused(true);
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("pausecommand.success.setTitle") + player.getPlayingTrack().getInfo().title);
            success.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
        }
    }

    @Override
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("pausecommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("pausecommand.gethelp");
    }


}

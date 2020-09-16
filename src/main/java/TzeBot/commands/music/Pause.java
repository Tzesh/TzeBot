package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Pause implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        final AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("pause.error.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("pause.error.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            player.setPaused(true);
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.pause") + TzeBot.essentials.LanguageDetector.getMessage("pause.success.setTitle") + player.getPlayingTrack().getInfo().title);
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
        }
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("pause.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("pause.gethelp");
    }


}

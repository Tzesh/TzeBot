package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.GuildMusicManager;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class Stop implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        if (!musicManager.player.isPaused()) {
            musicManager.scheduler.getQueue().clear();
            musicManager.player.stopTrack();
            musicManager.player.setPaused(false);

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.stop") + TzeBot.essentials.LanguageDetector.getMessage("stop.success.setTitle"));
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            ctx.getChannel().sendTyping().queue();
            ctx.getChannel().sendMessage(success.build()).queue();
            success.clear();
        } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("stop.error.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("stop.error.setDescription"));

            ctx.getChannel().sendTyping().queue();
            ctx.getChannel().sendMessage(error.build()).queue();
            error.clear();
        }



    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("stop.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("stop.gethelp");
    }
}

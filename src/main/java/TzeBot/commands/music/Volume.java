package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Volume implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        String input = String.join(" ", ctx.getArgs());
        if (input.equals("")) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("volume.error.setTitle"));
                error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("volume.error.setDescription") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() +"%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
        }
        
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.volume") + TzeBot.essentials.LanguageDetector.getMessage("volume.success.setTitle") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
        success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("volume.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("volume.gethelp");
    }
}

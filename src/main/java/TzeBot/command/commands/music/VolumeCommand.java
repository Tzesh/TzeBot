package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        String input = String.join(" ", ctx.getArgs());
        if (input.equals("")) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("volumecommand.error.setTitle"));
                error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("volumecommand.error.setDescription") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() +"%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
        }
        
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("volumecommand.success.setTitle") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
        success.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();
    }

    @Override
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("volumecommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("volumecommand.gethelp");
    }
}

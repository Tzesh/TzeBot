package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Volume implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager manager = PlayerManager.getInstance();
        final TextChannel channel = ctx.getChannel();

        final String input = String.join(" ", ctx.getArgs());
        if (isInteger(input)) {
            if (Integer.parseInt(input) > 100 || Integer.parseInt(input) < 0) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("volume.error.setTitle"));
                error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("volume.error.setDescription") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() +"%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            } else {
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
                Config.VOLUMES.put(ctx.getGuild().getIdLong(), Integer.parseInt(input));
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.volume") + TzeBot.essentials.LanguageDetector.getMessage("volume.success.setTitle") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
                success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
            }
        } else {
            if (input.equals("")) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("volume.error.setTitle"));
                error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("volume.error.setDescription") + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() +"%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
        }
            if (ctx.getArgs().size() > 1) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("play.noargs.setDescription1") + Config.get("pre") + TzeBot.essentials.LanguageDetector.getMessage("play.noargs.setDescription2"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        }
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("volume.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("volume.gethelp");
    }
    
    boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
            }
        }
}

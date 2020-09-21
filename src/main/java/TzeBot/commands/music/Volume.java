package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Volume implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager manager = PlayerManager.getInstance();
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        final String input = String.join(" ", ctx.getArgs());
        if (isInteger(input)) {
            if (Integer.parseInt(input) > 100 || Integer.parseInt(input) < 0) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("volume.error.setTitle", guildID));
                error.setDescription(LanguageDetector.getMessage("volume.error.setDescription", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            } else {
                manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
                Config.VOLUMES.put(ctx.getGuild().getIdLong(), Integer.parseInt(input));
                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageDetector.getMessage("general.icon.volume", guildID) + LanguageDetector.getMessage("volume.success.setTitle", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");
                success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
            }
        } else {
            if (input.equals("")) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("volume.error.setTitle", guildID));
                error.setDescription(LanguageDetector.getMessage("volume.error.setDescription", guildID) + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }
            if (ctx.getArgs().size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.403", guildID));
                error.setDescription(LanguageDetector.getMessage("play.noargs.setDescription1", guildID) + Config.get("pre") + LanguageDetector.getMessage("play.noargs.setDescription2", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("volume.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("volume.gethelp", guildID);
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

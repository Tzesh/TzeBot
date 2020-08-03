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
                error.setTitle("❌ You can arrange the volume between [1-100]%.");
                error.setDescription("Current volume is: " + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() +"%.");

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
        }
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(input));
        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle("✅ New volume is set to: " + manager.getGuildMusicManager(ctx.getGuild()).player.getVolume() + "%.");
        succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());



        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getHelp() {
        return "Sets the volume of player.";
    }
}

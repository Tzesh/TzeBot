package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ I cannot leave.");
            error.setDescription("That may be caused I even didn't join a voice channel.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ I cannot leave.");
            error.setDescription("You have to be connected to the channel which I've connected.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        audioManager.closeAudioConnection();
        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle("✅ Leaving your voice channel.");
        succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();

    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Makes the bot leave your channel.";
    }
}

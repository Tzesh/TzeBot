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
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("leavecommand.cannotleave.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("leavecommand.notconnected"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.error") + TzeBot.Languages.LanguageDetector.getMessage("leavecommand.cannotleave.setTitle"));
            error.setDescription(TzeBot.Languages.LanguageDetector.getMessage("leavecommand.notin"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        audioManager.closeAudioConnection();
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(TzeBot.Languages.LanguageDetector.getMessage("general.icon.success") + TzeBot.Languages.LanguageDetector.getMessage("leavecommand.success.setTitle"));
        success.setFooter(TzeBot.Languages.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());


        channel.sendTyping().queue();
        channel.sendMessage(success.build()).queue();
        success.clear();

    }

    @Override
    public String getName() {
        return TzeBot.Languages.LanguageDetector.getMessage("leavecommand.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.Languages.LanguageDetector.getMessage("leavecommand.gethelp");
    }
}

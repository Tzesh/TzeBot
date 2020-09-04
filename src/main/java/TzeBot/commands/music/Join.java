package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Join implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager manager = PlayerManager.getInstance();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("join.alreadyconnected.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("join.alreadyconnected.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("join.joinchannel.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("join.joinchannel.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfmember = ctx.getGuild().getSelfMember();

        if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("join.cannotjoin.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("join.cannotjoin.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(5);
        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.join") + TzeBot.essentials.LanguageDetector.getMessage("join.success.setTitle"));
        succes.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("join.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("join.gethelp");
    }
}

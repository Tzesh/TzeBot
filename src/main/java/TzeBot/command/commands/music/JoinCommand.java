package TzeBot.command.commands.music;

import TzeBot.command.CommandContext;
import TzeBot.command.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager manager = PlayerManager.getInstance();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Already connected.");
            error.setDescription("Already connected a voice channel.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle("❌ Please join a voice channel.");
            error.setDescription("You have to be connected to a voice channel to request me to join you.");

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
            error.setTitle("❌ I cannot join.");
            error.setDescription("Because I don't have the permission to join specified voice channel.");

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(5);
        EmbedBuilder succes = new EmbedBuilder();
        succes.setColor(0x00ff00);
        succes.setTitle("✅ Joining your voice channel.");
        succes.setFooter("By the command of " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendTyping().queue();
        channel.sendMessage(succes.build()).queue();
        succes.clear();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Makes the bot join your channel.";
    }
}

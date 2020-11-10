package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Instant;

import static TzeBot.essentials.LanguageManager.getMessage;

public class Join implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final PlayerManager manager = PlayerManager.getInstance();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final long guildID = ctx.getGuild().getIdLong();

        if (audioManager.isConnected()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.alreadyconnected.setTitle", guildID));
            error.setDescription(getMessage("join.alreadyconnected.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.joinchannel.setTitle", guildID));
            error.setDescription(getMessage("join.joinchannel.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }

        VoiceChannel voiceChannel = memberVoiceState.getChannel();
        Member selfmember = ctx.getGuild().getSelfMember();

        if (!selfmember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("join.cannotjoin.setTitle", guildID));
            error.setDescription(getMessage("join.cannotjoin.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }

        audioManager.openAudioConnection(voiceChannel);
        audioManager.setSelfDeafened(true);
        int volume = Config.VOLUMES.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> 50);
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(volume);
        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(getMessage("general.icon.join", guildID) + getMessage("join.success.setTitle", guildID));
        success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
        success.setTimestamp(Instant.now());

        channel.sendMessage(success.build()).queue();
    }

    @Override
    public String getName(long guildID) {
        return getMessage("join.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("join.gethelp", guildID);
    }
}

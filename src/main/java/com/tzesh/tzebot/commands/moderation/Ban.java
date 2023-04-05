package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ban implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final Member selfmember = ctx.getSelfMember();
        final long guildID = ctx.getGuild().getIdLong();
        final String message = String.join(" ", args);

        if (args.isEmpty() || ctx.getMessage().getMentions().getMembers().isEmpty() || ctx.getMessage().getMentions().getMembers().size() != 1 || args.size() < 1) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("ban.wrong.title", guildID));
            error.setDescription(LanguageManager.getMessage("ban.wrong.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("ban.permission1.title", guildID));
            error.setDescription(LanguageManager.getMessage("ban.permission1.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        if (!selfmember.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("ban.permission2.title", guildID));
            error.setDescription(LanguageManager.getMessage("ban.permission2.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        Member banRequested = ctx.getMessage().getMentions().getMembers().get(0);

        if (!selfmember.canInteract(banRequested) || !member.canInteract(banRequested)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.hierarchy", guildID));
            error.setDescription(LanguageManager.getMessage("ban.hierarchy.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        String reason = message.replace(banRequested + " ", "");

        if (args.size() != 1) banRequested.ban(1, TimeUnit.DAYS)
                .reason(String.format(LanguageManager.getMessage("ban.banned.with"), member, reason)).queue();
        else banRequested.ban(1, TimeUnit.DAYS)
                .reason(String.format(LanguageManager.getMessage("ban.banned.without"), member)).queue();

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + banRequested.getEffectiveName() + " " + LanguageManager.getMessage("ban.success.title", guildID));
        success.setDescription(args.size() == 1 ? LanguageManager.getMessage("ban.success.description1", guildID) : LanguageManager.getMessage("ban.success.description2", guildID) + reason);
        success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
        success.setTimestamp(Instant.now());

        channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
        success.clear();
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("ban.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("ban.help", guildID);
    }
}
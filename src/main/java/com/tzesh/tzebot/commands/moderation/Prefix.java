package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.Config;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.List;

public class Prefix implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
            error.setDescription(LanguageManager.getMessage("general.403.description", guildID));

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        Config.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder();
        success.setColor(0x00ff00);
        success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + " " + LanguageManager.getMessage("prefix.success.setTitle", guildID) + "`" + newPrefix + "`");
        success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

        channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("prefix.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("prefix.gethelp1", guildID)
                + "\n" + LanguageManager.getMessage("prefix.gethelp2", guildID) + Config.get("pre") + LanguageManager.getMessage("prefix.gethelp3", guildID);
    }
}

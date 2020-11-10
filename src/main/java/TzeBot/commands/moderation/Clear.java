package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;
import java.util.List;

public class Clear implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final Member selfmember = ctx.getSelfMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }
        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageManager.getMessage("general.nonperm.message_manage", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();

        } else if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
            error.setDescription(LanguageManager.getMessage("general.403.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
        } else {
            try {
                List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(String.join(" ", args)) + 1).complete();
                channel.deleteMessages(messages).queue();

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + String.join("", args) + " " + LanguageManager.getMessage("clear.successful.setTitle", guildID));
                success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(success.build()).queue();
                success.clear();
            } catch (IllegalArgumentException exception) {
                if (exception.toString().startsWith("java.lang.IllegalArgumentException")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("clear.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("clear.unsuitable.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("clear.older.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("clear.older.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                }
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("clear.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("clear.gethelp", guildID);
    }
}

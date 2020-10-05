package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Clear implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final Member selfmember = ctx.getGuild().getSelfMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageDetector.getMessage("general.not_authorized.description", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageDetector.getMessage("general.nonperm.message_manage", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.403", guildID));
            error.setDescription(LanguageDetector.getMessage("general.403.description", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        } else {
            try {
                List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(String.join(" ", args)) + 1).complete();
                channel.deleteMessages(messages).queue();

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageDetector.getMessage("general.icon.success", guildID) + String.join("", args) + " " + LanguageDetector.getMessage("clear.successful.setTitle", guildID));
                success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                ctx.getChannel().sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
            } catch (IllegalArgumentException exception) {
                if (exception.toString().startsWith("java.lang.IllegalArgumentException")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("clear.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("clear.unsuitable.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("clear.older.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("clear.older.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("clear.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("clear.gethelp", guildID);
    }
}

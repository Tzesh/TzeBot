package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * @author Tzesh
 */
public class Language implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final long guildID = ctx.getGuild().getIdLong();
        final String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
            error.setDescription(LanguageManager.getMessage("general.403.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();

        } else {
            if (Config.LANGUAGES.get(ctx.getGuild().getIdLong()).equals("en_en")) {
                if (args.get(0).toLowerCase().equals("turkish")) {
                    Config.LANGUAGES.put(ctx.getGuild().getIdLong(), "tr_tr");
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + LanguageManager.getMessage("language.successful.setTitle", guildID));
                    success.setDescription("Türkçe olarak komutlara göz atmak dilerseniz `" + prefix + "yardım` komudunu kullanabilirsiniz, if you want to revert language back to English just type `" + prefix + "dil ingilizce`.");
                    success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                    success.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
                    return;
                }
                if (args.get(0).toLowerCase().equals("english")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("language.already.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("language.already.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                    return;
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("language.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("language.unsuitable.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                    return;
                }
            }
            if (Config.LANGUAGES.get(ctx.getGuild().getIdLong()).equals("tr_tr")) {
                if (args.get(0).toLowerCase(new Locale("tr", "TR")).equals("ingilizce")) {
                    Config.LANGUAGES.put(ctx.getGuild().getIdLong(), "en_en");
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x00ff00);
                    success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + LanguageManager.getMessage("language.successful.setTitle", guildID));
                    success.setDescription("You can use `" + prefix + "help` command to look at the commands in English, if you want to revert language back to Turkish just type `" + prefix + "language turkish`.");
                    success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + " " + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                    success.setTimestamp(Instant.now());
                    channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
                    return;
                }
                if (LanguageManager.normalizer(args.get(0)).toLowerCase().equals("turkce")) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("language.already.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("language.already.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("language.unsuitable.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("language.unsuitable.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                }
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("language.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("language.gethelp", guildID);
    }

}

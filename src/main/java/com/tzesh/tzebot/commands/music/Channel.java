package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.Config;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.utils.EmojiUnicodes;
import com.tzesh.tzebot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;
import static com.tzesh.tzebot.utils.Controller.isExists;


/**
 * @author Tzesh
 */
public class Channel implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final String input = String.join(" ", ctx.getArgs());
        final Member selfmember = ctx.getSelfMember();
        final long guildID = ctx.getGuild().getIdLong();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }
        if (!selfmember.hasPermission(Permission.MANAGE_CHANNEL)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageManager.getMessage("general.nonperm.manage_channel", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        }
        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageManager.getMessage("general.nonperm.message_manage", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            return;
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("channel.noargs.setTitle", guildID));
            error.setDescription(LanguageManager.getMessage("channel.noargs.setDescription", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            if (args.size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
                error.setDescription(LanguageManager.getMessage("general.403.description", guildID));
                error.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                return;
            }
            if (input.equals(LanguageManager.getMessage("channel.create", guildID))) {
                if (isExists(ctx)) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("channel.already.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("channel.already.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
                    return;
                }
                ctx.getGuild().createTextChannel(LanguageManager.getMessage("music.icon", guildID) + LanguageManager.getMessage("music.name", guildID))
                        .setTopic(LanguageManager.getMessage("channel.setTopic", guildID))
                        .queue(textchannel -> {
                            Config.CHANNELCREATED.put(ctx.getGuild().getIdLong(), textchannel.getIdLong());
                        });

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageManager.getMessage("general.icon.success", guildID) + LanguageManager.getMessage("channel.success.setTitle", guildID));
                success.setDescription(LanguageManager.getMessage("channel.success.setDescription", guildID));
                success.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
                success.setTimestamp(Instant.now());

                channel.sendMessage(MessageCreateData.fromEmbeds(success.build())).queue();
                success.clear();
                return;
            }
            if (input.equals(LanguageManager.getMessage("channel.set", guildID))) {
                if (channel.getIdLong() == Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())) {
                    ctx.getMessage().delete().queue();
                    channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();

                    EmbedBuilder success1 = new EmbedBuilder();
                    success1.setColor(0xcccccc);
                    success1.setTitle(LanguageManager.getMessage("music.icon", guildID) + " " + LanguageManager.getMessage("channel.setTitle", guildID));
                    success1.setDescription(LanguageManager.getMessage("channel.firstMessage", guildID));
                    success1.addField(LanguageManager.getMessage("general.icon.nowplaying", guildID), LanguageManager.getMessage("channel.pauseresume", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.stop", guildID), LanguageManager.getMessage("stop.gethelp", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.skip", guildID), LanguageManager.getMessage("skip.gethelp", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.loop", guildID), LanguageManager.getMessage("loop.gethelp", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.shuffle", guildID), LanguageManager.getMessage("shuffle.gethelp", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.next", guildID), LanguageManager.getMessage("channel.next", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.previous", guildID), LanguageManager.getMessage("channel.previous", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.volumedown", guildID), LanguageManager.getMessage("channel.volumedown", guildID), true);
                    success1.addField(LanguageManager.getMessage("general.icon.volume", guildID), LanguageManager.getMessage("channel.volume", guildID), true);
                    success1.addBlankField(true);
                    success1.addField(LanguageManager.getMessage("general.icon.queue", guildID), LanguageManager.getMessage("channel.queuenp", guildID), true);
                    success1.addBlankField(true);
                    success1.setFooter(LanguageManager.getMessage("channel.setFooter", guildID));
                    channel.sendMessage(MessageCreateData.fromEmbeds(success1.build())).queue(message -> {
                        message.addReaction(EmojiUnicodes.nowPlaying.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.stop.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.skip.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.loop.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.shuffle.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.next.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.previous.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.volumedown.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.volumeup.getUnicode()).queue();
                        message.addReaction(EmojiUnicodes.queue.getUnicode()).queue();
                        HashMap<Long, Long> IDs = new HashMap<>();
                        IDs.put(channel.getIdLong(), message.getIdLong());
                        Config.MUSICCHANNELS.put(ctx.getGuild().getIdLong(), IDs);
                        Config.CHANNELS.put(ctx.getGuild().getIdLong(), channel.getIdLong());
                    });
                    success1.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("channel.wrongchannel.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("channel.wrongchannel.setDescription", guildID));
                    error.setTimestamp(Instant.now());
                    channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();

                }
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("channel.noargs.setTitle", guildID));
                error.setDescription(LanguageManager.getMessage("channel.noargs.setDescription", guildID));
                error.setTimestamp(Instant.now());
                channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("channel.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("channel.gethelp", guildID);
    }
}

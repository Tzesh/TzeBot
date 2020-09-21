/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageDetector;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 *
 * @author Tzesh
 */
public class Channel implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final String input = String.join(" ", ctx.getArgs());
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
        if (!selfmember.hasPermission(Permission.MANAGE_CHANNEL)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageDetector.getMessage("general.nonperm.manage_channel", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (!selfmember.hasPermission(Permission.MANAGE_EMOTES)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageDetector.getMessage("general.nonperm.manage_emotes", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageDetector.getMessage("general.nonperm.message_manage", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("channel.noargs.setTitle", guildID));
            error.setDescription(LanguageDetector.getMessage("channel.noargs.setDescription", guildID));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        } else {
            if (args.size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("general.403", guildID));
                error.setDescription(LanguageDetector.getMessage("general.403.description", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }
            if (input.equals(LanguageDetector.getMessage("channel.create", guildID))) {
                if (isExists(ctx)) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("channel.already.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("channel.already.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                }
                ctx.getGuild().createTextChannel(LanguageDetector.getMessage("music.icon", guildID) + LanguageDetector.getMessage("music.name", guildID))
                        .setTopic(LanguageDetector.getMessage("channel.setTopic", guildID))
                        .queue(textchannel -> {
                            Config.CHANNELCREATED.put(ctx.getGuild().getIdLong(), textchannel.getIdLong());
                        });

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageDetector.getMessage("general.icon.success", guildID) + LanguageDetector.getMessage("channel.success.setTitle", guildID));
                success.setDescription(LanguageDetector.getMessage("channel.success.setDescription", guildID));
                success.setFooter(LanguageDetector.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
                return;
            }
            if (input.equals(LanguageDetector.getMessage("channel.set", guildID))) {
                if (channel.getIdLong() == Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())) {
                    ctx.getMessage().delete().queue();
                    channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();

                    EmbedBuilder success1 = new EmbedBuilder();
                    success1.setColor(0xcccccc);
                    success1.setTitle(LanguageDetector.getMessage("music.icon", guildID) + " " + LanguageDetector.getMessage("channel.setTitle", guildID));
                    success1.setDescription(LanguageDetector.getMessage("channel.firstMessage", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.join", guildID) + ": " + LanguageDetector.getMessage("join.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.leave", guildID) + ": " + LanguageDetector.getMessage("leave.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.pause", guildID) + ": " + LanguageDetector.getMessage("pause.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.stop", guildID) + ": " + LanguageDetector.getMessage("stop.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.play", guildID) + ": " + LanguageDetector.getMessage("resume.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.nowplaying", guildID) + ": " + LanguageDetector.getMessage("nowplaying.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.skip", guildID) + ": " + LanguageDetector.getMessage("skip.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.loop", guildID) + ": " + LanguageDetector.getMessage("loop.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.shuffle", guildID) + ": " + LanguageDetector.getMessage("shuffle.gethelp", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.next", guildID) + ": " + LanguageDetector.getMessage("channel.next", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.previous", guildID) + ": " + LanguageDetector.getMessage("channel.previous", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.volume", guildID) + ": " + LanguageDetector.getMessage("channel.volume", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.volumedown", guildID) + ": " + LanguageDetector.getMessage("channel.volumedown", guildID)
                            + "\n" + LanguageDetector.getMessage("general.icon.queue", guildID) + ": " + LanguageDetector.getMessage("queue.gethelp", guildID));
                    success1.setFooter(LanguageDetector.getMessage("channel.setFooter", guildID));
                    channel.sendMessage(success1.build()).queue(message -> {
                        message.addReaction(LanguageDetector.getMessage("general.icon.join", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.leave", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.pause", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.stop", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.play", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.nowplaying", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.skip", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.loop", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.shuffle", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.next", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.previous", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.volume", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.volumedown", guildID)).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.queue", guildID)).queue();
                        HashMap<Long, Long> IDs = new HashMap<>();
                        IDs.put(channel.getIdLong(), message.getIdLong());
                        Config.MUSICCHANNELS.put(ctx.getGuild().getIdLong(), IDs);
                        Config.CHANNELS.put(ctx.getGuild().getIdLong(), channel.getIdLong());
                    });
                    success1.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("channel.wrongchannel.setTitle", guildID));
                    error.setDescription(LanguageDetector.getMessage("channel.wrongchannel.setDescription", guildID));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error", guildID) + LanguageDetector.getMessage("channel.noargs.setTitle", guildID));
                error.setDescription(LanguageDetector.getMessage("channel.noargs.setDescription", guildID));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageDetector.getMessage("channel.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageDetector.getMessage("channel.gethelp", guildID);
    }

    public boolean isExists(CommandContext ctx) {
        if (Config.CHANNELCREATED.containsKey(ctx.getGuild().getIdLong())) {
            return ctx.getGuild().getChannels().contains(ctx.getGuild().getTextChannelById(Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())));
        }
        return false;
    }

}

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package TzeBot.commands.music;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import java.util.HashMap;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import static TzeBot.utils.Controller.isExists;
import static TzeBot.essentials.LanguageDetector.getMessage;


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
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.not_authorized", guildID));
            error.setDescription(getMessage("general.not_authorized.description", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        if (!selfmember.hasPermission(Permission.MANAGE_CHANNEL)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.nonperm", guildID));
            error.setDescription(getMessage("general.nonperm.manage_channel", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (!selfmember.hasPermission(Permission.MANAGE_EMOTES)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.nonperm", guildID));
            error.setDescription(getMessage("general.nonperm.manage_emotes", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (!selfmember.hasPermission(Permission.MESSAGE_MANAGE)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.nonperm", guildID));
            error.setDescription(getMessage("general.nonperm.message_manage", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(getMessage("general.icon.error", guildID) + getMessage("channel.noargs.setTitle", guildID));
            error.setDescription(getMessage("channel.noargs.setDescription", guildID));

            
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        } else {
            if (args.size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("general.403", guildID));
                error.setDescription(getMessage("general.403.description", guildID));

                
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }
            if (input.equals(getMessage("channel.create", guildID))) {
                if (isExists(ctx)) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("channel.already.setTitle", guildID));
                    error.setDescription(getMessage("channel.already.setDescription", guildID));

                    
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                }
                ctx.getGuild().createTextChannel(getMessage("music.icon", guildID) + getMessage("music.name", guildID))
                        .setTopic(getMessage("channel.setTopic", guildID))
                        .queue(textchannel -> {
                            Config.CHANNELCREATED.put(ctx.getGuild().getIdLong(), textchannel.getIdLong());
                        });

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(getMessage("general.icon.success", guildID) + getMessage("channel.success.setTitle", guildID));
                success.setDescription(getMessage("channel.success.setDescription", guildID));
                success.setFooter(getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                
                channel.sendMessage(success.build()).queue();
                success.clear();
                return;
            }
            if (input.equals(getMessage("channel.set", guildID))) {
                if (channel.getIdLong() == Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())) {
                    ctx.getMessage().delete().queue();
                    channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();

                    EmbedBuilder success1 = new EmbedBuilder();
                    success1.setColor(0xcccccc);
                    success1.setTitle(getMessage("music.icon", guildID) + " " + getMessage("channel.setTitle", guildID));
                    success1.setDescription(getMessage("channel.firstMessage", guildID)
                            + "\n" + getMessage("general.icon.join", guildID) + ": " + getMessage("join.gethelp", guildID)
                            + "\n" + getMessage("general.icon.leave", guildID) + ": " + getMessage("leave.gethelp", guildID)
                            + "\n" + getMessage("general.icon.pause", guildID) + ": " + getMessage("pause.gethelp", guildID)
                            + "\n" + getMessage("general.icon.stop", guildID) + ": " + getMessage("stop.gethelp", guildID)
                            + "\n" + getMessage("general.icon.play", guildID) + ": " + getMessage("resume.gethelp", guildID)
                            + "\n" + getMessage("general.icon.nowplaying", guildID) + ": " + getMessage("nowplaying.gethelp", guildID)
                            + "\n" + getMessage("general.icon.skip", guildID) + ": " + getMessage("skip.gethelp", guildID)
                            + "\n" + getMessage("general.icon.loop", guildID) + ": " + getMessage("loop.gethelp", guildID)
                            + "\n" + getMessage("general.icon.shuffle", guildID) + ": " + getMessage("shuffle.gethelp", guildID)
                            + "\n" + getMessage("general.icon.next", guildID) + ": " + getMessage("channel.next", guildID)
                            + "\n" + getMessage("general.icon.previous", guildID) + ": " + getMessage("channel.previous", guildID)
                            + "\n" + getMessage("general.icon.volume", guildID) + ": " + getMessage("channel.volume", guildID)
                            + "\n" + getMessage("general.icon.volumedown", guildID) + ": " + getMessage("channel.volumedown", guildID)
                            + "\n" + getMessage("general.icon.queue", guildID) + ": " + getMessage("queue.gethelp", guildID));
                    success1.setFooter(getMessage("channel.setFooter", guildID));
                    channel.sendMessage(success1.build()).queue(message -> {
                        message.addReaction(getMessage("general.icon.join", guildID)).queue();
                        message.addReaction(getMessage("general.icon.leave", guildID)).queue();
                        message.addReaction(getMessage("general.icon.pause", guildID)).queue();
                        message.addReaction(getMessage("general.icon.stop", guildID)).queue();
                        message.addReaction(getMessage("general.icon.play", guildID)).queue();
                        message.addReaction(getMessage("general.icon.nowplaying", guildID)).queue();
                        message.addReaction(getMessage("general.icon.skip", guildID)).queue();
                        message.addReaction(getMessage("general.icon.loop", guildID)).queue();
                        message.addReaction(getMessage("general.icon.shuffle", guildID)).queue();
                        message.addReaction(getMessage("general.icon.next", guildID)).queue();
                        message.addReaction(getMessage("general.icon.previous", guildID)).queue();
                        message.addReaction(getMessage("general.icon.volume", guildID)).queue();
                        message.addReaction(getMessage("general.icon.volumedown", guildID)).queue();
                        message.addReaction(getMessage("general.icon.queue", guildID)).queue();
                        HashMap<Long, Long> IDs = new HashMap<>();
                        IDs.put(channel.getIdLong(), message.getIdLong());
                        Config.MUSICCHANNELS.put(ctx.getGuild().getIdLong(), IDs);
                        Config.CHANNELS.put(ctx.getGuild().getIdLong(), channel.getIdLong());
                    });
                    success1.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(getMessage("general.icon.error", guildID) + getMessage("channel.wrongchannel.setTitle", guildID));
                    error.setDescription(getMessage("channel.wrongchannel.setDescription", guildID));

                    
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(getMessage("general.icon.error", guildID) + getMessage("channel.noargs.setTitle", guildID));
                error.setDescription(getMessage("channel.noargs.setDescription", guildID));

                
                channel.sendMessage(error.build()).queue();
                error.clear();
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return getMessage("channel.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("channel.gethelp", guildID);
    }
}

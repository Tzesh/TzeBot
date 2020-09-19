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

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("general.not_authorized"));
            error.setDescription(LanguageDetector.getMessage("general.not_authorized.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        }
        if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("channel.noargs.setTitle"));
            error.setDescription(LanguageDetector.getMessage("channel.noargs.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        } else {
            if (args.size() > 1) {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("general.403"));
                error.setDescription(LanguageDetector.getMessage("general.403.description"));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
                return;
            }
            if (input.equals(LanguageDetector.getMessage("channel.create"))) {
                if (isExists(ctx)) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("channel.already.setTitle"));
                    error.setDescription(LanguageDetector.getMessage("channel.already.setDescription"));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                    return;
                }
                ctx.getGuild().createTextChannel(LanguageDetector.getMessage("music.icon") + LanguageDetector.getMessage("music.name"))
                        .setTopic(LanguageDetector.getMessage("channel.setTopic"))
                        .queue(textchannel -> {
                            Config.CHANNELCREATED.put(ctx.getGuild().getIdLong(), textchannel.getIdLong());
                        });

                EmbedBuilder success = new EmbedBuilder();
                success.setColor(0x00ff00);
                success.setTitle(LanguageDetector.getMessage("general.icon.success") + LanguageDetector.getMessage("channel.success.setTitle"));
                success.setDescription(LanguageDetector.getMessage("channel.success.setDescription"));
                success.setFooter(LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

                channel.sendTyping().queue();
                channel.sendMessage(success.build()).queue();
                success.clear();
                return;
            }
            if (input.equals(LanguageDetector.getMessage("channel.set"))) {
                if (channel.getIdLong() == Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())) {
                    ctx.getMessage().delete().queue();
                    channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();

                    EmbedBuilder success1 = new EmbedBuilder();
                    success1.setColor(0xcccccc);
                    success1.setTitle(LanguageDetector.getMessage("music.icon") + " " + LanguageDetector.getMessage("channel.setTitle"));
                    success1.setDescription(LanguageDetector.getMessage("channel.firstMessage")
                            + "\n" + LanguageDetector.getMessage("general.icon.join") + ": " + LanguageDetector.getMessage("join.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.leave") + ": " + LanguageDetector.getMessage("leave.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.pause") + ": " + LanguageDetector.getMessage("pause.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.stop") + ": " + LanguageDetector.getMessage("stop.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.play") + ": " + LanguageDetector.getMessage("resume.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.nowplaying") + ": " + LanguageDetector.getMessage("nowplaying.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.skip") + ": " + LanguageDetector.getMessage("skip.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.loop") + ": " + LanguageDetector.getMessage("loop.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.shuffle") + ": " + LanguageDetector.getMessage("shuffle.gethelp")
                            + "\n" + LanguageDetector.getMessage("general.icon.next") + ": " + LanguageDetector.getMessage("channel.next")
                            + "\n" + LanguageDetector.getMessage("general.icon.previous") + ": " + LanguageDetector.getMessage("channel.previous")
                            + "\n" + LanguageDetector.getMessage("general.icon.volume") + ": " + LanguageDetector.getMessage("channel.volume")
                            + "\n" + LanguageDetector.getMessage("general.icon.volumedown") + ": " + LanguageDetector.getMessage("channel.volumedown")
                            + "\n" + LanguageDetector.getMessage("general.icon.queue") + ": " + LanguageDetector.getMessage("queue.gethelp"));
                    success1.setFooter(LanguageDetector.getMessage("channel.setFooter"));
                    channel.sendMessage(success1.build()).queue(message -> {
                        message.addReaction(LanguageDetector.getMessage("general.icon.join")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.leave")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.pause")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.stop")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.play")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.nowplaying")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.skip")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.loop")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.shuffle")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.next")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.previous")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.volume")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.volumedown")).queue();
                        message.addReaction(LanguageDetector.getMessage("general.icon.queue")).queue();
                        HashMap<Long, Long> IDs = new HashMap<>();
                        IDs.put(channel.getIdLong(), message.getIdLong());
                        Config.MUSICCHANNELS.put(ctx.getGuild().getIdLong(), IDs);
                        Config.CHANNELS.put(ctx.getGuild().getIdLong(), channel.getIdLong());
                    });
                    success1.clear();
                } else {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("channel.wrongchannel.setTitle"));
                    error.setDescription(LanguageDetector.getMessage("channel.wrongchannel.setDescription"));

                    channel.sendTyping().queue();
                    channel.sendMessage(error.build()).queue();
                    error.clear();
                }
            } else {
                EmbedBuilder error = new EmbedBuilder();
                error.setColor(0xff3923);
                error.setTitle(LanguageDetector.getMessage("general.icon.error") + LanguageDetector.getMessage("channel.noargs.setTitle"));
                error.setDescription(LanguageDetector.getMessage("channel.noargs.setDescription"));

                channel.sendTyping().queue();
                channel.sendMessage(error.build()).queue();
                error.clear();
            }
        }
    }

    @Override
    public String getName() {
        return LanguageDetector.getMessage("channel.name");
    }

    @Override
    public String getHelp() {
        return LanguageDetector.getMessage("channel.gethelp");
    }

    public boolean isExists(CommandContext ctx) {
        if (Config.CHANNELCREATED.containsKey(ctx.getGuild().getIdLong())) {
            return ctx.getGuild().getChannels().contains(ctx.getGuild().getTextChannelById(Config.CHANNELCREATED.get(ctx.getGuild().getIdLong())));
        }
        return false;
    }

}

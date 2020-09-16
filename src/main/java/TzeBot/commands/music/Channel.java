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

/**
 *
 * @author Tzesh
 */
public class Channel implements ICommand {
    private long channelID;

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final String input = String.join(" ", ctx.getArgs());
        
        
        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.not_authorized"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.not_authorized.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
        } if (Config.MUSICCHANNELS.containsKey(ctx.getGuild().getIdLong())) {
            HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> null);
            boolean isExists = true;
            for (long channelID : IDs.keySet()) {
                isExists = ctx.getGuild().getChannels().contains(ctx.getChannel().getGuild().getGuildChannelById(channelID));
            }
            if (isExists) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("channel.already.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("channel.already.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            } else {
                Config.isCreated = false;
            }
            } if (args.isEmpty()) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            }
        
        else {
            if (args.size() > 1) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            }
            if (input.equals(TzeBot.essentials.LanguageDetector.getMessage("channel.create"))) {
            HashMap<Long, Long> IDs = Config.MUSICCHANNELS.computeIfAbsent(ctx.getGuild().getIdLong(), (id) -> null);
            if (Config.isCreated || Config.MUSICCHANNELS.containsKey(ctx.getGuild().getIdLong())) {
            boolean isExists = true;
            if (IDs != null) {
            for (long channelID : IDs.keySet()) {
                isExists = ctx.getGuild().getChannels().contains(ctx.getChannel().getGuild().getGuildChannelById(channelID));
            }
            }
            if (isExists) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("channel.already.setTitle"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("channel.already.setDescription"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            return;
            }
            }
            Config.isCreated = true;
            ctx.getGuild().createTextChannel(TzeBot.essentials.LanguageDetector.getMessage("music.icon") + TzeBot.essentials.LanguageDetector.getMessage("music.name"))
            .setTopic(TzeBot.essentials.LanguageDetector.getMessage("channel.setTopic"))
            .queue();
            
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0x00ff00);
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.success") + TzeBot.essentials.LanguageDetector.getMessage("channel.success.setTitle"));
            success.setDescription(TzeBot.essentials.LanguageDetector.getMessage("channel.success.setDescription"));
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("general.bythecommand") + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());

            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue();
            success.clear();
            return;
            }
            if (input.equals(TzeBot.essentials.LanguageDetector.getMessage("channel.set"))) {
            ctx.getMessage().delete().queue();
            channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();
            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0xcccccc);
            success.setTitle(TzeBot.essentials.LanguageDetector.getMessage("music.icon") + " " + TzeBot.essentials.LanguageDetector.getMessage("channel.setTitle"));
            success.setDescription(TzeBot.essentials.LanguageDetector.getMessage("channel.firstMessage")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.join") + ": " + TzeBot.essentials.LanguageDetector.getMessage("join.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.leave") + ": " + TzeBot.essentials.LanguageDetector.getMessage("leave.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.pause") + ": " + TzeBot.essentials.LanguageDetector.getMessage("pause.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.stop") + ": " + TzeBot.essentials.LanguageDetector.getMessage("stop.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.play") + ": " + TzeBot.essentials.LanguageDetector.getMessage("resume.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.nowplaying") + ": " + TzeBot.essentials.LanguageDetector.getMessage("nowplaying.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.skip")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("skip.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.loop")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("loop.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.shuffle")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("shuffle.gethelp")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.next")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("channel.next")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.previous")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("channel.previous")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.volume")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("channel.volume")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.volumedown")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("channel.volumedown")
                            + "\n" + TzeBot.essentials.LanguageDetector.getMessage("general.icon.queue")  + ": " + TzeBot.essentials.LanguageDetector.getMessage("queue.gethelp"));
            success.setFooter(TzeBot.essentials.LanguageDetector.getMessage("channel.setFooter"));
            channel.sendTyping().queue();
            channel.sendMessage(success.build()).queue(message -> {
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.join")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.leave")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.pause")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.stop")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.play")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.nowplaying")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.skip")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.loop")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.shuffle")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.next")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.previous")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.volume")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.volumedown")).queue();
            message.addReaction(TzeBot.essentials.LanguageDetector.getMessage("general.icon.queue")).queue();
            HashMap<Long, Long> IDs = new HashMap<>();
            IDs.put(channel.getIdLong(), message.getIdLong());
            Config.MUSICCHANNELS.put(ctx.getGuild().getIdLong(), IDs);
            Config.CHANNELS.put(ctx.getGuild().getIdLong(), channel.getIdLong());
            });
            success.clear();
            } else {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(TzeBot.essentials.LanguageDetector.getMessage("general.icon.error") + TzeBot.essentials.LanguageDetector.getMessage("general.403"));
            error.setDescription(TzeBot.essentials.LanguageDetector.getMessage("general.403.description"));

            channel.sendTyping().queue();
            channel.sendMessage(error.build()).queue();
            error.clear();
            }
        }
    }

    @Override
    public String getName() {
        return TzeBot.essentials.LanguageDetector.getMessage("channel.name");
    }

    @Override
    public String getHelp() {
        return TzeBot.essentials.LanguageDetector.getMessage("channel.gethelp");
    }
    
    public void setChannel(long ID) {
        channelID = ID;
    }
}

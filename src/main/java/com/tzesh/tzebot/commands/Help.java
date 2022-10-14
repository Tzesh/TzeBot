package com.tzesh.tzebot.commands;

import com.tzesh.tzebot.essentials.CommandContext;
import com.tzesh.tzebot.essentials.CommandManager;
import com.tzesh.tzebot.essentials.Config;
import com.tzesh.tzebot.essentials.ICommand;
import com.tzesh.tzebot.essentials.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;
import java.util.List;

import static com.tzesh.tzebot.essentials.LanguageManager.getMessage;

public class Help implements ICommand {

    private final CommandManager manager;

    public Help(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        final String prefix = Config.PREFIXES.get(ctx.getGuild().getIdLong());
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageManager.getMessage("help.info.setTitle", guildID), "https://tzesh.github.io/TzeBot/");
            info.setDescription(LanguageManager.getMessage("help.info.setDescription", guildID));
            info.addField(LanguageManager.getMessage("help.info.setDescription1", guildID), "`" + prefix + LanguageManager.getMessage("help.name", guildID) + " " + LanguageManager.getMessage("moderation.name", guildID) + "`", true);
            info.addField(LanguageManager.getMessage("help.info.setDescription2", guildID), "`" + prefix + LanguageManager.getMessage("help.name", guildID) + " " + LanguageManager.getMessage("music.name", guildID) + "`", true);
            info.addField(LanguageManager.getMessage("help.info.setDescription3", guildID), "`" + prefix + LanguageManager.getMessage("support.name", guildID) + "`", true);
            info.setImage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/embed_logo.png");
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());
            info.setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (args.get(0).equals(LanguageManager.getMessage("music.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageManager.getMessage("help.music.info.setTitle", guildID));
            info.setDescription(LanguageManager.getMessage("help.music.info.setDescription", guildID));
            info.addField(LanguageManager.getMessage("music.icon", guildID) + "`" + prefix + LanguageManager.getMessage("channel.name", guildID) + "`", LanguageManager.getMessage("channel.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.join", guildID) + "`" + prefix + LanguageManager.getMessage("join.name", guildID) + "`", LanguageManager.getMessage("join.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.leave", guildID) + "`" + prefix + LanguageManager.getMessage("leave.name", guildID) + "`", LanguageManager.getMessage("leave.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.play", guildID) + "`" + prefix + LanguageManager.getMessage("play.name", guildID) + "`", LanguageManager.getMessage("play.gethelp1", guildID) + ". " + LanguageManager.getMessage("play.gethelp2", guildID) + prefix + LanguageManager.getMessage("play.name", guildID) + LanguageManager.getMessage("play.gethelp3", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.pause", guildID) + "`" + prefix + LanguageManager.getMessage("pause.name", guildID) + "`", LanguageManager.getMessage("pause.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.play", guildID) + "`" + prefix + LanguageManager.getMessage("resume.name", guildID) + "`", LanguageManager.getMessage("resume.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.stop", guildID) + "`" + prefix + LanguageManager.getMessage("stop.name", guildID) + "`", LanguageManager.getMessage("stop.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.skip", guildID) + "`" + prefix + LanguageManager.getMessage("skip.name", guildID) + "`", LanguageManager.getMessage("skip.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.volume", guildID) + "`" + prefix + LanguageManager.getMessage("volume.name", guildID) + "`", LanguageManager.getMessage("volume.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.loop", guildID) + "`" + prefix + LanguageManager.getMessage("loop.name", guildID) + "`", LanguageManager.getMessage("loop.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.nowplaying", guildID) + "`" + prefix + LanguageManager.getMessage("nowplaying.name", guildID) + "`", LanguageManager.getMessage("nowplaying.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.queue", guildID) + "`" + prefix + LanguageManager.getMessage("queue.name", guildID) + "`", LanguageManager.getMessage("queue.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.next", guildID) + "`" + prefix + LanguageManager.getMessage("seek.name", guildID) + "`" + LanguageManager.getMessage("general.icon.previous", guildID), LanguageManager.getMessage("seek.gethelp", guildID), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());

            info.setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());
            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (args.get(0).equals(LanguageManager.getMessage("moderation.name", guildID))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageManager.getMessage("help.moderation.info.setTitle", guildID));
            info.setDescription(LanguageManager.getMessage("help.moderation.info.setDescription", guildID));
            info.addField(LanguageManager.getMessage("general.icon.prefix", guildID) + "`" + prefix + LanguageManager.getMessage("prefix.name", guildID) + "`", LanguageManager.getMessage("prefix.gethelp1", guildID) + " " + LanguageManager.getMessage("prefix.gethelp2", guildID) + prefix + LanguageManager.getMessage("prefix.gethelp3", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.clear", guildID) + "`" + prefix + LanguageManager.getMessage("clear.name", guildID) + "`", LanguageManager.getMessage("clear.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.ban", guildID) + "`" + prefix + LanguageManager.getMessage("ban.name", guildID) + "`", LanguageManager.getMessage("ban.description", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.language", guildID) + "`" + prefix + LanguageManager.getMessage("language.name", guildID) + "`", LanguageManager.getMessage("language.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.vote", guildID) + "`" + prefix + LanguageManager.getMessage("vote.name", guildID) + "`", LanguageManager.getMessage("vote.gethelp", guildID), true);
            info.addField(LanguageManager.getMessage("general.icon.vote", guildID) + "`" + prefix + LanguageManager.getMessage("voterole.name", guildID) + "`", LanguageManager.getMessage("voterole.gethelp", guildID), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());
            info.setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), ctx.getSelfMember().getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search, guildID);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.404", guildID));
            error.setDescription(LanguageManager.getMessage("general.404.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(0xffffff);
            help.setTitle(command.getName(guildID) + LanguageManager.getMessage("general.icon.question"));
            help.setDescription(command.getHelp(guildID));
            help.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + ctx.getMember().getUser().getName(), ctx.getMember().getUser().getAvatarUrl());
            help.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(help.build())).queue();
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("help.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("help.gethelp1", guildID) + "\n"
                + LanguageManager.getMessage("help.gethelp2", guildID) + Config.get("pre") + LanguageManager.getMessage("help.gethelp3", guildID);
    }
}

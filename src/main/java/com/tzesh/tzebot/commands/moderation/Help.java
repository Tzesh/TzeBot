package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.core.*;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import java.time.Instant;

/**
 * A command to get help about the bot and its commands
 */
public class Help extends AbstractCommand<MessageReceivedEvent> {

    private final CommandManager manager;

    public Help(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    protected void initializePreRequisites() {
        boolean isArgsCorrect = args.size() <= 1;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
    }

    @Override
    public void handleCommand() {

        if (args.isEmpty()) {
            EmbedBuilder info = new EmbedBuilder()
                    .setTitle(LanguageManager.getMessage("help.info.setTitle", guildID), "https://tzesh.github.io/TzeBot/")
                    .setDescription(LanguageManager.getMessage("help.info.setDescription", guildID))
                    .addField(LanguageManager.getMessage("help.info.setDescription1", guildID), "`" + prefix + LanguageManager.getMessage("help.name", guildID) + " " + LanguageManager.getMessage("moderation.name", guildID) + "`", true)
                    .addField(LanguageManager.getMessage("help.info.setDescription2", guildID), "`" + prefix + LanguageManager.getMessage("help.name", guildID) + " " + LanguageManager.getMessage("music.name", guildID) + "`", true)
                    .setImage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/embed_logo.png")
                    .setColor(0x6699ff)
                    .setTimestamp(Instant.now())
                    .setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), selfMember.getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        String arg = args.get(0);

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("music.name", guildID))) {
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

            info.setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), selfMember.getUser().getAvatarUrl());
            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("moderation.name", guildID))) {
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
            info.setFooter(LanguageManager.getMessage("help.info.setFooter", guildID), selfMember.getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        Command command = manager.getCommand(arg, guildID);

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
            help.setFooter(LanguageManager.getMessage("general.bythecommand", guildID) + selfMember.getUser().getName(), selfMember.getUser().getAvatarUrl());
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
        return LanguageManager.getMessage("help.gethelp1", guildID) + "\n" + LanguageManager.getMessage("help.gethelp2", guildID) + ConfigurationManager.getEnvKey("pre") + LanguageManager.getMessage("help.gethelp3", guildID);
    }
}

package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.core.command.CommandManager;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.core.language.LanguageManager;
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
                    .setTitle(LanguageManager.getMessage("help.info.setTitle", this.guildChannel.getLanguage()), "https://tzesh.github.io/TzeBot/")
                    .setDescription(LanguageManager.getMessage("help.info.setDescription", this.guildChannel.getLanguage()))
                    .addField(LanguageManager.getMessage("help.info.setDescription1", this.guildChannel.getLanguage()), "`" + prefix + LanguageManager.getMessage("help.name", this.guildChannel.getLanguage()) + " " + LanguageManager.getMessage("moderation.name", this.guildChannel.getLanguage()) + "`", true)
                    .addField(LanguageManager.getMessage("help.info.setDescription2", this.guildChannel.getLanguage()), "`" + prefix + LanguageManager.getMessage("help.name", this.guildChannel.getLanguage()) + " " + LanguageManager.getMessage("music.name", this.guildChannel.getLanguage()) + "`", true)
                    .setImage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/embed_logo.png")
                    .setColor(0x6699ff)
                    .setTimestamp(Instant.now())
                    .setFooter(LanguageManager.getMessage("help.info.setFooter", this.guildChannel.getLanguage()), selfMember.getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        String arg = args.get(0);

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("music.name", this.guildChannel.getLanguage()))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageManager.getMessage("help.music.info.setTitle", this.guildChannel.getLanguage()));
            info.setDescription(LanguageManager.getMessage("help.music.info.setDescription", this.guildChannel.getLanguage()));
            info.addField(LanguageManager.getMessage("music.icon", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("channel.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("channel.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.join", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("join.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("join.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.leave", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("leave.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("leave.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.play", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("play.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("play.gethelp1", this.guildChannel.getLanguage()) + ". " + LanguageManager.getMessage("play.gethelp2", this.guildChannel.getLanguage()) + prefix + LanguageManager.getMessage("play.name", this.guildChannel.getLanguage()) + LanguageManager.getMessage("play.gethelp3", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.pause", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("pause.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("pause.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.play", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("resume.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("resume.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.stop", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("stop.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("stop.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.skip", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("skip.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("skip.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.volume", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("volume.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("volume.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.loop", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("loop.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("loop.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.nowplaying", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("nowplaying.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("nowplaying.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.queue", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("queue.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("queue.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.next", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("seek.name", this.guildChannel.getLanguage()) + "`" + LanguageManager.getMessage("general.icon.previous", this.guildChannel.getLanguage()), LanguageManager.getMessage("seek.gethelp", this.guildChannel.getLanguage()), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());

            info.setFooter(LanguageManager.getMessage("help.info.setFooter", this.guildChannel.getLanguage()), selfMember.getUser().getAvatarUrl());
            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("moderation.name", this.guildChannel.getLanguage()))) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle(LanguageManager.getMessage("help.moderation.info.setTitle", this.guildChannel.getLanguage()));
            info.setDescription(LanguageManager.getMessage("help.moderation.info.setDescription", this.guildChannel.getLanguage()));
            info.addField(LanguageManager.getMessage("general.icon.prefix", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("prefix.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("prefix.gethelp1", this.guildChannel.getLanguage()) + " " + LanguageManager.getMessage("prefix.gethelp2", this.guildChannel.getLanguage()) + prefix + LanguageManager.getMessage("prefix.gethelp3", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.clear", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("clear.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("clear.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.ban", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("ban.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("ban.description", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.language", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("language.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("language.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.vote", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("vote.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("vote.gethelp", this.guildChannel.getLanguage()), true);
            info.addField(LanguageManager.getMessage("general.icon.vote", this.guildChannel.getLanguage()) + "`" + prefix + LanguageManager.getMessage("voterole.name", this.guildChannel.getLanguage()) + "`", LanguageManager.getMessage("voterole.gethelp", this.guildChannel.getLanguage()), true);
            info.setColor(0x6699ff);
            info.setTimestamp(Instant.now());
            info.setFooter(LanguageManager.getMessage("help.info.setFooter", this.guildChannel.getLanguage()), selfMember.getUser().getAvatarUrl());

            channel.sendMessage(MessageCreateData.fromEmbeds(info.build())).queue();
            return;
        }

        Command command = manager.getCommand(arg);

        if (command == null) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", this.guildChannel.getLanguage()) + LanguageManager.getMessage("general.404", this.guildChannel.getLanguage()));
            error.setDescription(LanguageManager.getMessage("general.404.description", this.guildChannel.getLanguage()));
            error.setTimestamp(Instant.now());

            channel.sendMessage(MessageCreateData.fromEmbeds(error.build())).queue();
        } else {
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(0xffffff);
            help.setTitle(command.getName(guildID) + LanguageManager.getMessage("general.icon.question"));
            help.setDescription(command.getHelp(guildID));
            help.setFooter(LanguageManager.getMessage("general.bythecommand", this.guildChannel.getLanguage()) + selfMember.getUser().getName(), selfMember.getUser().getAvatarUrl());
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

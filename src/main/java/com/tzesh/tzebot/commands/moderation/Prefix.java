package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * A command to change the prefix of the bot in a guild
 * @author tzesh
 * @see Command
 * @see AbstractCommand
 */
public class Prefix extends AbstractCommand<MessageReceivedEvent> {

    @Override
    protected void initializePreRequisites() {
        boolean memberHasPermission = member.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(memberHasPermission, "general.not_authorized", "general.not_authorized.description");
        if (!memberHasPermission) return;

        boolean isArgsCorrect = args.size() == 1;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
    }

    @Override
    public void handleCommand() {
        final String newPrefix = args.get(0);

        this.guildChannel.setPrefix(newPrefix);
        this.guildChannel.save();

        String successTitle = LanguageManager.getMessage("prefix.success.setTitle", this.guildChannel.getLanguage()) + "`" + newPrefix + "`";

        sendMessage(EmbedMessageBuilder.createCustomSuccessMessage(successTitle, "", user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("prefix.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("prefix.gethelp1", guildID)
                + "\n" + LanguageManager.getMessage("prefix.gethelp2", guildID) + ConfigurationManager.getEnvKey("pre") + LanguageManager.getMessage("prefix.gethelp3", guildID);
    }
}

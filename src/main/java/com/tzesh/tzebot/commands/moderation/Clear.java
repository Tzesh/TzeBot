package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.InputControlHelper;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;


/**
 * A command to clear messages from a channel
 * @author tzesh
 * @see Command
 * @see AbstractCommand
 */
public class Clear extends AbstractCommand<MessageReceivedEvent> {

    @Override
    protected void initializePreRequisites() {
        boolean memberHasPermission = member.hasPermission(Permission.MESSAGE_MANAGE);
        addPreRequisite(memberHasPermission, "general.not_authorized", "general.not_authorized.description");
        if (!memberHasPermission) return;

        boolean selfMemberHasPermission = selfMember.hasPermission(Permission.MESSAGE_MANAGE);
        addPreRequisite(selfMemberHasPermission, "general.nonperm", "general.nonperm.message_manage");
        if (!selfMemberHasPermission) return;

        boolean isArgsCorrect = args.size() == 1 && InputControlHelper.isInteger(args.get(0));
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
    }

    @Override
    public void handleCommand() {
        try {
            // amount of messages to delete added 1 due to the command message
            int amount = Integer.parseInt(args.get(0)) + 1;

            // get messages to be deleted
            List<Message> messages = channel.getHistory().retrievePast(amount).complete();

            // delete the messages
            channel.deleteMessages(messages).queue();

            // send success message
            sendMessage(
                    EmbedMessageBuilder.createCustomSuccessMessage(
                            LanguageManager.getMessage("general.icon.success", this.guildChannel.getLanguage()) + amount + " " + LanguageManager.getMessage("clear.successful.setTitle", this.guildChannel.getLanguage()),
                            "",
                            user,
                            guildChannel
                    )
            );
        } catch (IllegalArgumentException exception) {
            // send error message
            sendMessage(EmbedMessageBuilder.createErrorMessage("clear.older.setTitle", "clear.older.setDescription", user, this.guildChannel));
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("clear.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("clear.gethelp", guildID);
    }
}

package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * A command to vote on a question
 * @author Tzesh
 */
public class Vote extends AbstractCommand<MessageReceivedEvent> {

    @Override
    protected void initializePreRequisites() {
        boolean memberHasPermission = member.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(memberHasPermission, "general.not_authorized", "general.not_authorized.description");
        if (!memberHasPermission) return;

        boolean selfMemberHasPermission = selfMember.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(selfMemberHasPermission, "general.nonperm", "general.nonperm.message_manage");
        if (!selfMemberHasPermission) return;

        String[] variables = rawMessage.split(" : ");
        boolean isArgsCorrect = variables.length >= 3 && variables.length <= 10;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
    }

    @Override
    public void handleCommand() {
        String[] variables = rawMessage.split(" : ");
        String question = variables[0];
        String iconKey = "general.icon.vote";
        String title = question + LanguageManager.getMessage("general.icon.question", this.guildChannel.getLanguage());
        String description = buildDescription(variables);

        MessageEmbed embedMessage = EmbedMessageBuilder.createCustomMessageWithoutReadyMessage(0x0087ff, title, description, iconKey, user, this.guildChannel);
        channel.sendMessage(MessageCreateData.fromEmbeds(embedMessage)).queue(message -> {
            for (int i = 1; i < variables.length; i++) {
                message.addReaction(Emoji.fromUnicode(LanguageManager.getMessage("general.icon." + i, this.guildChannel))).queue();
            }
        });
    }

    private String buildDescription(String[] variables) {
        StringBuilder description = new StringBuilder();
        for (int i = 1; i < variables.length; i++) {
            description.append(LanguageManager.getMessage("general.icon." + i, this.guildChannel)).append(" ").append(variables[i]).append("\n");
        }

        return description.toString();
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("vote.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("vote.gethelp", guildID);
    }

}

package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import java.util.LinkedList;
import java.util.List;

/**
 * A command to vote on a question to get a role
 *
 * @author Tzesh
 */
public class VoteRole extends AbstractCommand<MessageReceivedEvent> {

    @Override
    protected void initializePreRequisites() {
        boolean memberHasPermission = member.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(memberHasPermission, "general.not_authorized", "general.not_authorized.description");
        if (!memberHasPermission) return;

        boolean selfMemberHasPermission = selfMember.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(selfMemberHasPermission, "general.nonperm", "general.nonperm.manage_roles");
        if (!selfMemberHasPermission) return;

        int mentionedRoles = message.getMentions().getRoles().size();
        boolean isArgsCorrect = mentionedRoles >= 2 && mentionedRoles <= 4 && rawMessage.split(" : ").length == mentionedRoles + 3;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
        if (!isArgsCorrect) return;

        boolean canSelfMemberInteract = canSelfMemberInteractWithRoles(message.getMentions().getRoles());
        addPreRequisite(canSelfMemberInteract, "general.hierarchy", "general.hierarchy.setDescription");
    }

    @Override
    public void handleCommand() {
        final LinkedList<Long> roleIDs = new LinkedList<>();

        String[] variables = rawMessage.split(" : ");

        for (Role role : message.getMentions().getRoles()) {
            roleIDs.add(role.getIdLong());
        }

        String question = variables[0];
        String iconKey = "general.icon.vote";
        String title = question + LanguageManager.getMessage("general.icon.question", this.guildChannel.getLanguage());
        String description = buildDescription(variables);

        MessageEmbed embedMessage = EmbedMessageBuilder.createCustomMessageWithoutReadyMessage(0x0087ff, title, description, iconKey, user, this.guildChannel);
        channel.sendMessage(MessageCreateData.fromEmbeds(embedMessage)).queue(message -> {
            for (int i = 1; i < variables.length; i++) {
                message.addReaction(Emoji.fromUnicode(LanguageManager.getMessage("general.icon." + i, this.guildChannel))).queue();
            }

            this.guildChannel.setVoteRoleMessageID(message.getIdLong());
            this.guildChannel.setVoteRoleIDs(roleIDs);
        });
    }

    private boolean canSelfMemberInteractWithRoles(List<Role> roles) {
        for (Role role : roles) {
            if (!selfMember.canInteract(role)) {
                return false;
            }
        }

        return true;
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
        return LanguageManager.getMessage("voterole.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("voterole.gethelp", guildID);
    }
}

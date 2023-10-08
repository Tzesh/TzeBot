package com.tzesh.tzebot.listeners.message.messagereaction.voterole;

import com.tzesh.tzebot.listeners.message.messagereaction.GenericMessageReactionEventListener;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;

import static com.tzesh.tzebot.core.inventory.Inventory.VOTE_ROLE_CHANNELS;

/**
 * This class is used to listen to the message reaction add event to add roles to the user
 * @author tzesh
 */
public class AddRoleEventListener extends GenericMessageReactionEventListener<MessageReactionAddEvent> {

    @Override
    protected boolean canHandle(MessageReactionAddEvent event) {
        boolean isBot = event.getUser().isBot();
        boolean doesVoteRoleExist = VOTE_ROLE_CHANNELS.containsKey(event.getMessageIdLong());

        return !isBot && doesVoteRoleExist;
    }

    @Override
    protected void handle(MessageReactionAddEvent event) {
        final List<Long> roleIDs = VOTE_ROLE_CHANNELS.get(event.getMessageIdLong());
        final String getFormattedEmoji = this.getFormattedEmoji(event);
        final int roleCount = roleIDs.size();
        final int roleIndex = getRoleIndexByEmoji(getFormattedEmoji, roleCount);

        if (roleIndex == -1) return;

        addRole(event, roleIDs, roleIndex);
    }

    private void addRole(MessageReactionAddEvent event, List<Long> roleIDs, int index) {
        LOGGER.info("Adding role {} to user {}", event.getGuild().getRoleById(roleIDs.get(index)).getName(), event.getMember().getEffectiveName());

        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(index))).queue();
    }
}

package com.tzesh.tzebot.listeners.message.messagereaction.voterole;

import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.listeners.message.messagereaction.GenericMessageReactionEventListener;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.List;

import static com.tzesh.tzebot.core.inventory.Inventory.VOTE_ROLE_CHANNELS;

/**
 * This is a simple event listener for message reaction remove events
 * @author tzesh
 */
public class RemoveRoleEventListener extends GenericMessageReactionEventListener<MessageReactionRemoveEvent> {

    @Override
    protected boolean canHandle(MessageReactionRemoveEvent event) {
        boolean isBot = event.getUser().isBot();
        boolean doesVoteRoleExist = VOTE_ROLE_CHANNELS.containsKey(event.getMessageIdLong());

        return !isBot && doesVoteRoleExist;
    }

    @Override
    protected void handle(MessageReactionRemoveEvent event) {
        final List<Long> roleIDs = VOTE_ROLE_CHANNELS.get(event.getMessageIdLong());
        final String getFormattedEmoji = this.getFormattedEmoji(event);
        final int roleCount = roleIDs.size();
        final int roleIndex = getRoleIndexByEmoji(getFormattedEmoji, roleCount);

        if (roleIndex == -1) return;

        removeRole(event, roleIDs, roleIndex);
    }

    private void removeRole(MessageReactionRemoveEvent event, List<Long> roleIDs, int index) {
        LOGGER.info("Removing role {} from user {}", event.getGuild().getRoleById(roleIDs.get(index)).getName(), event.getMember().getEffectiveName());

        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(index))).queue();
    }
}

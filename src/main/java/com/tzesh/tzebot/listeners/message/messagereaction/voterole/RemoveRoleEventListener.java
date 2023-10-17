package com.tzesh.tzebot.listeners.message.messagereaction.voterole;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.message.messagereaction.GenericMessageReactionEventListener;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.List;

/**
 * This is a simple event listener for message reaction remove events
 * @author tzesh
 */
public class RemoveRoleEventListener extends GenericMessageReactionEventListener<MessageReactionRemoveEvent> {

    @Override
    protected boolean canHandle(MessageReactionRemoveEvent event) {
        boolean isBot = event.getUser().isBot();
        final GuildChannel guildChannel = Inventory.get(event.getGuild().getIdLong());
        boolean doesVoteRoleExist = guildChannel.doesVoteRoleChannelExist() && guildChannel.getVoteRoleMessageID().equals(event.getMessageIdLong());

        return !isBot && doesVoteRoleExist;
    }

    @Override
    protected void handle(MessageReactionRemoveEvent event) {
        final GuildChannel guildChannel = Inventory.get(event.getGuild().getIdLong());
        final List<Long> roleIDs = guildChannel.getVoteRoleIDs();
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

package com.tzesh.tzebot.listeners.message.messagereaction.voterole;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.listeners.message.messagereaction.GenericMessageReactionEventListener;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;

/**
 * This class is used to listen to the message reaction add event to add roles to the user
 * @author tzesh
 */
public class AddRoleEventListener extends GenericMessageReactionEventListener<MessageReactionAddEvent> {

    @Override
    protected boolean canHandle(MessageReactionAddEvent event) {
        boolean isBot = event.getUser().isBot();
        final GuildChannel guildChannel = Inventory.get(event.getGuild().getIdLong());
        boolean doesVoteRoleExist = guildChannel.doesVoteRoleChannelExist() && guildChannel.getVoteRoleMessageID().equals(event.getMessageIdLong());

        return !isBot && doesVoteRoleExist;
    }

    @Override
    protected void handle(MessageReactionAddEvent event) {
        final GuildChannel guildChannel = Inventory.get(event.getGuild().getIdLong());
        final List<Long> roleIDs = guildChannel.getVoteRoleIDs();
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

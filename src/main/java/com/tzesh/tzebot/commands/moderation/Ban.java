package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.commands.abstracts.Command;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

/**
 * A command to ban a member from the server
 * @author tzesh
 * @see Command
 * @see AbstractCommand
 */
public class Ban extends AbstractCommand<MessageReceivedEvent> {

    @Override
    protected void initializePreRequisites() {
        boolean isArgsCorrect = message.getMentions().getMembers().size() == 1 && args.size() >= 1;
        addPreRequisite(isArgsCorrect, "ban.wrong.title", "ban.wrong.description");
        if (!isArgsCorrect) return;

        boolean doesMemberHavePermission = member.hasPermission(Permission.BAN_MEMBERS);
        addPreRequisite(doesMemberHavePermission, "ban.permission1.title", "ban.permission1.description");
        if (!doesMemberHavePermission) return;

        boolean doesSelfMemberHavePermission = selfMember.hasPermission(Permission.BAN_MEMBERS);
        addPreRequisite(doesSelfMemberHavePermission, "ban.permission2.title", "ban.permission2.description");
        if (!doesMemberHavePermission) return;

        boolean isMemberHigherThanSelfMember = selfMember.canInteract(member);
        addPreRequisite(isMemberHigherThanSelfMember, "general.hierarchy", "ban.hierarchy.setDescription");
    }

    @Override
    public void handleCommand() {
        Member banRequested = message.getMentions().getMembers().get(0);
        String reason = rawMessage.replace(banRequested + " ", "");

        if (args.size() != 1) banRequested.ban(1, TimeUnit.DAYS)
                .reason(String.format(LanguageManager.getMessage("ban.banned.with"), member, reason)).queue();
        else banRequested.ban(1, TimeUnit.DAYS)
                .reason(String.format(LanguageManager.getMessage("ban.banned.without"), member)).queue();

        String successTitle = banRequested.getEffectiveName() + " " + LanguageManager.getMessage("ban.success.title", this.guildChannel.getLanguage());
        String successDescription = args.size() == 1 ? LanguageManager.getMessage("ban.success.description1", this.guildChannel.getLanguage()) : LanguageManager.getMessage("ban.success.description2", this.guildChannel.getLanguage()) + reason;

        sendMessage(EmbedMessageBuilder.createCustomSuccessMessage(successTitle, successDescription, user, this.guildChannel));
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("ban.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("ban.help", guildID);
    }
}
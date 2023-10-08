package com.tzesh.tzebot.commands.moderation;

import com.tzesh.tzebot.commands.abstracts.AbstractCommand;
import com.tzesh.tzebot.core.LanguageManager;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

/**
 * Language command to change the language of the bot in a guild
 * @author Tzesh
 */
public class Language extends AbstractCommand<MessageReceivedEvent> {
    Map<String, String> languages;

    @Override
    protected void initializePreRequisites() {
        boolean isArgsCorrect = args.size() == 1;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
        if (!isArgsCorrect) return;

        boolean doesMemberHavePermission = member.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(doesMemberHavePermission, "general.not_authorized", "general.not_authorized.description");
        if (!doesMemberHavePermission) return;

        boolean doesSelfMemberHavePermission = selfMember.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(doesSelfMemberHavePermission, "general.nonperm", "general.nonperm.message_manage");
        if (!doesSelfMemberHavePermission) return;

        initializeLanguages();
        boolean isLanguageCorrect = checkLanguage(args.get(0));
        addPreRequisite(isLanguageCorrect, "general.403", "general.403.description");
        if (!isLanguageCorrect) return;

        boolean isLanguageAlreadySet = !languages.get(args.get(0).toLowerCase()).equals(Inventory.LANGUAGES.get(guildID).toLowerCase());
        addPreRequisite(isLanguageAlreadySet, "language.already.setTitle", "language.already.setDescription");
    }

    @Override
    public void handleCommand() {
        String languageAlias = args.get(0).toLowerCase();
        String desiredLanguage = languages.get(languageAlias);

        Inventory.LANGUAGES.put(guildID, desiredLanguage);
        sendMessage(EmbedMessageBuilder.createSuccessMessage("language.successful.setTitle", "", user, guildID));
    }

    private void initializeLanguages() {
        this.languages = new java.util.HashMap<>();
        languages.put("english", "en_en");
        languages.put("turkish", "tr_tr");
    }

    private boolean checkLanguage(String arg) {
        return languages.containsKey(arg.toLowerCase());
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("language.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("language.gethelp", guildID);
    }

}

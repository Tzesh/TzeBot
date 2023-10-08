package com.tzesh.tzebot.commands.abstracts;

import com.tzesh.tzebot.core.CommandContextImpl;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.List;

/**
 * An interface for comments
 * @author tzesh
 * @see CommandContextImpl
 * @see Command
 * @see AbstractCommand
 */
public interface Command<T extends GenericMessageEvent> {

    void handle(CommandContextImpl<T> ctx); // To get all the functions of onGuildMessageReceivedEvent

    String getName(long guildID); // Name of the code -> LanguageDetector.get("command.name", guildID);

    String getHelp(long guildID); // Help line of the code but both are will be called in LanguageDetector.get("command.gethelp", guildID);

    default List<String> getAliases() {
        return List.of();
    }
}

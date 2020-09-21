package TzeBot.essentials;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx); // To get all of the functions of onGuildMessageReceivedEvent

    String getName(long guildID); // Name of the code -> LanguageDetector.get("command.name", guildID);

    String getHelp(long guildID); // Help line of the code but both are will be called in LanguageDetector.get("command.gethelp", guildID);

    default List<String> getAliases() {
        return List.of();
    }
}

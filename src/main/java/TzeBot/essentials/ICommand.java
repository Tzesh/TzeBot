package TzeBot.essentials;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx); // To get all of the functions of onGuildMessageReceivedEvent

    String getName(); // Name of the code -> LanguageDetector.get("command.name");

    String getHelp(); // Help line of the code but both are will be called in LanguageDetector.get("command.gethelp");

    default List<String> getAliases() {
        return List.of();
    }
}

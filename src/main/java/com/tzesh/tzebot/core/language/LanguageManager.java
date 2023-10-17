package com.tzesh.tzebot.core.language;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.inventory.Inventory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.tzesh.tzebot.core.common.CommonConstants.DEFAULT_LANGUAGE;

/**
 * This is a class responsible for managing the language of the bot
 * @author Tzesh
 */
public class LanguageManager {
    private static final Gson gson = new Gson();
    private static Map<String, Map<String, String>> localizer = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageManager.class);

    public static void getMessages() {
        Path localizerPath = Path.of("localizer.json");

        if (Files.exists(localizerPath)) {
            String jsonString = "";

            try {
                jsonString = Files.readString(Path.of("localizer.json"));
            } catch (Exception ignored) {
                LOGGER.info("Reading of localizer.json file end with error.");
            }

            if (jsonString.isBlank() || jsonString.isEmpty()) getMessagesFromRepository();
            else {
                TypeToken<Map<String, Map<String, String>>> map = new TypeToken<>() {
                };
                localizer = gson.fromJson(jsonString, map.getType());
                LOGGER.info("All messages are loaded from 'localizer.json'. Supported languages are 'English' and 'Turkish'");
            }
        } else {
            getMessagesFromRepository();
        }
    }

    public static void getMessagesFromRepository() {
        URL localizerURL = null;
        try {
            localizerURL = new URL("https://raw.githubusercontent.com/Tzesh/TzeBot/master/localizer.json");
        } catch (MalformedURLException ignored) {
        }
        if (localizerURL == null) return;
        try {
            FileUtils.copyURLToFile(localizerURL, Files.createFile(Paths.get("localizer.json")).toFile());
        } catch (IOException ignored) {
        }
        getMessages();
    }

    public static String getMessage(String key, GuildChannel channel) {
        return getMessage(key, channel.getLanguage());
    }

    public static String getMessage(String key, String language) {
        if (Strings.isNullOrEmpty(key)) return "";

        String result = localizer.get(key).get(language);

        if (Strings.isNullOrEmpty(result)) return "An error occurred during getting message key: " + key + " language: " + language;
        return localizer.get(key).get(language);
    }

    public static String getMessage(String key, long guildID) {
        return getMessage(key, Inventory.get(guildID));
    }

    public static String getMessage(String key) {
        return localizer.get(key).get(DEFAULT_LANGUAGE);
    }
}

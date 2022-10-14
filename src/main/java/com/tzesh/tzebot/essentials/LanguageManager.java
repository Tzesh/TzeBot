package com.tzesh.tzebot.essentials;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tzesh
 */
public class LanguageManager extends ListenerAdapter {
    private static final Gson gson = new Gson();
    private static Map<String, Map<String, String>> localizer = new HashMap<>();

    public static void getMessages() {
        Path localizerPath = Path.of("localizer.json");

        if (Files.exists(localizerPath)) {
            String jsonString = "";

            try {
                jsonString = Files.readString(Path.of("localizer.json"));
            } catch (Exception ignored) {
                System.out.println("Reading of localizer.json file end with error.");
            }

            if (jsonString.isBlank() || jsonString.isEmpty()) getMessagesFromRepository();
            else {
                TypeToken<Map<String, Map<String, String>>> map = new TypeToken<>(){};
                localizer = gson.fromJson(jsonString, map.getType());
                System.out.println("All messages are loaded from 'localizer.json'. Supported languages are 'English' and 'Turkish'.");
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

    public static String getMessage(String key, long guildID) {
        String shortening = Config.LANGUAGES.computeIfAbsent(guildID, (id) -> "en_en");
        if (shortening.equals("en_en")) {
            return localizer.get(key).get("en_en");
        }
        else if (shortening.equals("tr_tr")) {
            return localizer.get(key).get("tr_tr");
        } else {
            return "Error_Message_Not_Found";
        }
    }

    public static String getMessage(String key) {
        return localizer.get(key).get("en_en");
    }

    public static String normalizer(String abnormal) {
        abnormal = Normalizer.normalize(abnormal, Normalizer.Form.NFD);
        abnormal = abnormal.replaceAll("[^\\p{ASCII}]", "");
        abnormal = abnormal.replaceAll("\\p{M}", "");
        abnormal = abnormal.toLowerCase();
        return abnormal;
    }
}

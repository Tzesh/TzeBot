/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.essentials;

import TzeBot.essentials.Config;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashMap;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 *
 * @author Tzesh
 */
public class LanguageDetector extends ListenerAdapter {
    static long guildId;
    private static HashMap<String, String> en_en = new HashMap<>();
    private static HashMap<String, String> tr_tr = new HashMap<>();
    
    public static void getMessages() {
        try {
        URL tzegit = new URL("https://raw.githubusercontent.com/Tzesh/TzeBot/master/messages.txt");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(tzegit.openStream(), "UTF8"));
        String inputLine = in.readLine();
        while ((inputLine = in.readLine()) != null) {
            String[] array = inputLine.split(",  ");
                if (array[0].equals("en_en")) {
                   en_en.put(array[1], array[2]);
                } if (array[0].equals("tr_tr")) {
                   tr_tr.put(array[1], array[2]);
                }
        }
        in.close();
        System.out.println("All messages are loaded from github. Supported languages are 'English' and 'Turkish'.");
        } catch (Exception e) {
            System.out.println("An error occured during the getting messages from github.");
            System.exit(0);
        }
    }
    
    public static void setGuild(long guildId) {
        LanguageDetector.guildId = guildId;
    }
    
    public static String getMessage(String key) {
        Config.LANGUAGES.get(guildId);
        String shortening = Config.LANGUAGES.computeIfAbsent(guildId, (id) -> "en_en");
        if (shortening.equals("en_en")) {
            return en_en.get(key);
        } if (shortening.equals("tr_tr")) {
            return tr_tr.get(key);
        }
        else return "Error_Message_Not_Found";
    }
    
    public static String normalizer(String anormal) {
        anormal = Normalizer.normalize(anormal, Normalizer.Form.NFD);
        anormal = anormal.replaceAll("[^\\p{ASCII}]", "");
        anormal = anormal.replaceAll("\\p{M}", "");
        anormal = anormal.toLowerCase();
        return anormal;
    }
    
}

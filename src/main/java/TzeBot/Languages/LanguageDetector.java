/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot.Languages;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Tzesh
 */
public class LanguageDetector {
    public static String shortening;
    private static HashMap<String, String> en_en = new HashMap<>();
    private static HashMap<String, String> tr_tr = new HashMap<>();
    
    public static void selectLanguage(String shorten) {
        shortening = shorten;
    }
    
    public static void getMessages() {
        try (Scanner scanner = new Scanner(new BufferedReader( new InputStreamReader(new FileInputStream("messages.txt"), "UTF8")))) {
            while (scanner.hasNextLine()) {
                String information = scanner.nextLine();
                String[] array = information.split(", ");
                if (array[0].equals("en_en")) {
                   en_en.put(array[1], array[2]);
                } if (array[0].equals("tr_tr")) {
                   tr_tr.put(array[1], array[2]);
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println("File not found during getting messages.");
        } catch (IOException exception) {
            System.out.println("An error occured during getting mesages.");
        }
    }
    
    public static String getMessage(String key) {
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

package TzeBot.essentials;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.MINUTES;

public class Config {

    private static Dotenv dotenv = Dotenv.load();
    public static Map<Long, String> PREFIXES = new HashMap<>();
    public static Map<Long, String> LANGUAGES = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static int serverNumber = 0;
    public static double currentVersion = 1.8;
    
    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
    public static void save(String token, String pre, String owner, String key, String shards) {
         try (FileWriter writer = new FileWriter(".env")) {
            writer.write("TOKEN=" + token +
                    "\nPRE=" + pre +
                    "\nOWNER=" + owner +
                    "\nKEY=" + key +
                    "\nSHARD=" + shards);
        } catch (IOException exception) {
            System.out.println("An error occured during saving the .env");
        }
         System.out.println("All .env settings have been saved.");
         dotenv = Dotenv.load();
     }
    
    public static void saveDatabase() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("database.bin"))) {
                Map<Long, String> prefixDB = PREFIXES;
                out.writeObject(prefixDB);
                Map<Long, String> languagesDB = LANGUAGES;
                out.writeObject(languagesDB);
            } catch (IOException exception) {
                System.out.println("An error occurred during saving databases process...");
            }
                System.out.println("Prefixes and languages are saved into database.");
    }
    public static void getDatabase() {
        File file = new File("database.bin");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                PREFIXES = (HashMap<Long, String>)in.readObject();
                LANGUAGES = (HashMap<Long, String>)in.readObject();
            } catch (IOException exception) {
                System.out.println("An error occured during process...");
            } catch (ClassNotFoundException exception) {
                System.out.println("Class not found.");
            }
        } else System.out.println("Database file not found.");
        System.out.println("All languages and prefixed are loaded from database.");
        }

    public static void saveForAQuarter() {
        final Runnable beeper = new Runnable() {
                public void run() { saveDatabase(); }
            };
        ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 15, 15, MINUTES);
        System.out.println("Prefixes and languages are saved into database.");
    }
    
    public static boolean versionControl() {
        try {
        URL tzegit = new URL("https://raw.githubusercontent.com/Tzesh/TzeBot/master/versioncontrol.txt");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(tzegit.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] array = inputLine.split("=");
                
                if (array[0].equals("version")) {
                   if (currentVersion * 100 < Double.parseDouble(array[1]) * 100) {
                       System.out.println("You are currently using TzeBot v" + currentVersion);
                       System.out.println("Please update your TzeBot to latest version by downloading TzeBot-" + array[1] + ".rar from update button.");
                       return false;
                   }
                }
        }
        in.close();
        } catch (Exception e) {
            System.out.println("An error occured during the version control.");
        }
        return true;
    }
               
    }

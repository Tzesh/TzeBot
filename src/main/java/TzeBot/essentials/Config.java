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
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.MINUTES;

public class Config {

    private static final Dotenv dotenv = Dotenv.load(); // To get .env file properties which are unique for bot
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // To save databases per 15 minutes

    public static Map<Long, String> PREFIXES = new HashMap<>(); // All of the prefixes of the servers default is .env's prefix setting
    public static Map<Long, String> LANGUAGES = new HashMap<>(); // All of the languages of the servers default is English
    public static Map<Long, Long> CHANNELS = new HashMap<>(); // This was the part of the bound command that allows you to bound music channels to one channel.
    public static Map<Long, LinkedList<Long>> VOTEROLES = new HashMap<>(); // Voterole commands, we don't want to lose all of the message and role data when we restart the bot.
    public static Map<Long, HashMap<Long, Long>> MUSICCHANNELS = new HashMap<>(); // All of the music channels that are created and initialized.
    public static Map<Long, Integer> VOLUMES = new HashMap<>(); // Volumes of the servers default is 50%
    public static Map<Long, Long> CHANNELCREATED = new HashMap<>(); // All of the music channels that are created but either initialized or not. It's important value for preventing some kind of abusing of channel creation.

    public static int serverNumber = 0;
    public static double currentVersion = 2.3; // Check if there's an update or not.

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }

    public static void save(String token, String pre, String owner, String key, String shards) { // .env saving
        try (FileWriter writer = new FileWriter(".env")) {
            writer.write("TOKEN=" + token
                    + "\nPRE=" + pre
                    + "\nOWNER=" + owner
                    + "\nKEY=" + key
                    + "\nSHARD=" + shards);
        } catch (IOException exception) {
            System.out.println("An error occured during saving the .env");
        }
        System.out.println("All .env settings have been saved.");
    }

    public static void saveDatabase() { // database.bin saving
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("database.bin"))) {
            Map<Long, String> prefixDB = PREFIXES;
            out.writeObject(prefixDB);
            Map<Long, String> languagesDB = LANGUAGES;
            out.writeObject(languagesDB);
            Map<Long, Long> channelsDB = CHANNELS;
            out.writeObject(channelsDB);
            Map<Long, LinkedList<Long>> voterolesDB = VOTEROLES;
            out.writeObject(voterolesDB);
            Map<Long, HashMap<Long, Long>> musicchannelsDB = MUSICCHANNELS;
            out.writeObject(musicchannelsDB);
            Map<Long, Integer> volumesDB = VOLUMES;
            out.writeObject(volumesDB);
            Map<Long, Long> channelcreatedDB = CHANNELCREATED;
            out.writeObject(channelcreatedDB);
            out.reset();
            out.close();
        } catch (IOException exception) {
            System.out.println("An error occurred during saving databases process...");
        }
        System.out.println("Prefixes and languages are saved into database.");
    }

    public static void getDatabase() {
        File file = new File("database.bin");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                PREFIXES = (HashMap<Long, String>) in.readObject();
                LANGUAGES = (HashMap<Long, String>) in.readObject();
                CHANNELS = (HashMap<Long, Long>) in.readObject();
                VOTEROLES = (HashMap<Long, LinkedList<Long>>) in.readObject();
                MUSICCHANNELS = (HashMap<Long, HashMap<Long, Long>>) in.readObject();
                VOLUMES = (HashMap<Long, Integer>) in.readObject();
                CHANNELCREATED = (HashMap<Long, Long>) in.readObject();
                in.reset();
                in.close();
            } catch (IOException exception) {
                System.out.println("An error occured during process...");
            } catch (ClassNotFoundException exception) {
                System.out.println("Class not found.");
            }
        } else {
            System.out.println("Database file not found.");
        }
        System.out.println("All languages and prefixed are loaded from database.");
    }

    public static void saveForAQuarter() { // save database per 15 minutes
        final Runnable beeper = new Runnable() {
            public void run() {
                saveDatabase();
            }
        };
        ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 15, 15, MINUTES);
        System.out.println("Prefixes and languages are saving into database per 15 minutes.");
    }

    public static boolean versionControl() { // check if there is an update or not
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

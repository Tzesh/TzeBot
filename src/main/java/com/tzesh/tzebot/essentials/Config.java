package com.tzesh.tzebot.essentials;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Config {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load(); // To get .env file properties which are unique for bot
    public static Map<Long, String> PREFIXES = new HashMap<>(); // All the prefixes of the servers default is .env's prefix setting
    public static Map<Long, String> LANGUAGES = new HashMap<>(); // All the languages of the servers default is English
    public static Map<Long, Long> CHANNELS = new HashMap<>(); // This was the part of the bound command that allows you to bound music channels to one channel.
    public static Map<Long, LinkedList<Long>> VOTEROLES = new HashMap<>(); // Vote role commands, we don't want to lose messages and role data when we restart the bot.
    public static Map<Long, HashMap<Long, Long>> MUSICCHANNELS = new HashMap<>(); // Music channels that are created and initialized.
    public static Map<Long, Integer> VOLUMES = new HashMap<>(); // Volumes of the servers default is 50%
    public static Map<Long, Long> CHANNELCREATED = new HashMap<>(); // Music channels that are created but either initialized or not. It's important value for preventing some kind of abusing of channel creation.
    public static double currentVersion = 3.0; // Check if there's an update or not.
    public static String downloadURL = ""; // to download the latest release

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
            System.out.println("An error occurred during saving the .env");
        }
        Dotenv.load();
        System.out.println("All .env settings have been saved.");
    }

    public static void createENV() {
        File envFile = new File(".env");
        if (!envFile.exists()) {
            try (FileWriter writer = new FileWriter(".env")) {
                writer.write("TOKEN="
                        + "\nPRE=."
                        + "\nOWNER="
                        + "\nKEY="
                        + "\nSHARD=");
            } catch (IOException exception) {
                System.out.println("An error occurred during creating the .env");
            }
        }
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
        } catch (IOException exception) {
            System.out.println("An error occurred during saving databases process...");
        }
        System.out.println("Prefixes and languages are saved into database.");
    }

    public static void getDatabase() {
        File file = new File("database.bin");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                PREFIXES = (Map<Long, String>) in.readObject();
                LANGUAGES = (Map<Long, String>) in.readObject();
                CHANNELS = (Map<Long, Long>) in.readObject();
                VOTEROLES = (Map<Long, LinkedList<Long>>) in.readObject();
                MUSICCHANNELS = (Map<Long, HashMap<Long, Long>>) in.readObject();
                VOLUMES = (Map<Long, Integer>) in.readObject();
                CHANNELCREATED = (Map<Long, Long>) in.readObject();
            } catch (Exception exception) {
                System.out.println("An error occurred during process...");
            }
        } else {
            System.out.println("Database file not found.");
            return;
        }

        System.out.println("Database has been loaded.");
    }

    public static void saveForAQuarter() { // save database per 15 minutes
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(Config::saveDatabase, 0, 30, TimeUnit.MINUTES);
        System.out.println("Prefixes and languages are saving into database per 30 minutes.");
    }

    public static boolean versionControl() { // check if there is an update or not
        try {
            URL url = new URL("https://api.github.com/repos/tzesh/TzeBot/releases/latest");
            JSONTokener tokener = new JSONTokener(url.openStream());
            JSONObject release = new JSONObject(tokener);
            String tagName = release.getString("tag_name");
            JSONArray assets = release.getJSONArray("assets");
            JSONObject obj1 = (JSONObject) assets.get(0);
            downloadURL = obj1.get("browser_download_url").toString();
            if (currentVersion * 1000 < Double.parseDouble(tagName) * 1000) {
                System.out.println("You are currently using TzeBot v" + currentVersion);
                System.out.println("Please update your TzeBot to latest version by downloading TzeBot-" + tagName + ".zip from update button.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("An error occurred during the version control.");
        }
        return true;
    }

    public static void startBot(String apiKey, String token, int shards) throws LoginException {
        Config.saveForAQuarter();
        LanguageManager.getMessages();
        JDABuilder.createDefault(apiKey)
                .addEventListeners(new Listener())
                .setToken(token)
                .useSharding(0, shards)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.listening(".help").asRichPresence())
                .setCompression(Compression.NONE)
                .setAutoReconnect(true)
                .setLargeThreshold(300)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES)
                .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_INVITES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_PRESENCES)
                .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                .setChunkingFilter(ChunkingFilter.NONE)
                .build();
    }
}

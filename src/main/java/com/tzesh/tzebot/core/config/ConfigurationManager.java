package com.tzesh.tzebot.core.config;

import com.tzesh.tzebot.core.config.properties.ConfigurationProperties;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.core.inventory.strategy.LocalStoreStrategy;
import com.tzesh.tzebot.core.inventory.strategy.MongoDBStoreStrategy;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.core.adapter.EventAdapter;
import com.tzesh.tzebot.core.inventory.local.LocalInventoryManager;
import com.tzesh.tzebot.core.version.VersionController;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tzesh.tzebot.core.common.CommonConstants.DEFAULT_PREFIX;
import static com.tzesh.tzebot.core.common.CommonConstants.SAVE_INTERVAL;

/**
 * This class is for managing the configuration of the bot
 *
 * @author tzesh
 */
public class ConfigurationManager {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load(); // to get .env file properties which are unique for bot
    private final static LocalInventoryManager inventoryManager = new LocalInventoryManager(); // inventory manager
    private final static VersionController versionController = new VersionController(); // version controller
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class); // logger
    private final static ConfigurationProperties configurationProperties = new ConfigurationProperties(); // configuration properties

    public static String getEnvKey(String key) {
        return dotenv.get(key.toUpperCase());
    }

    public static void saveEnv(String token, String pre, String owner, String key, String shards) { // .env saving
        try (FileWriter writer = new FileWriter(".env")) {
            writer.write("TOKEN=" + token
                    + "\nPRE=" + pre
                    + "\nOWNER=" + owner
                    + "\nKEY=" + key
                    + "\nSHARD=" + shards
                    + "\nUSE_MONGODB=" + getEnvKey("USE_MONGODB")
                    + "\nMONGODB_URI=" + getEnvKey("MONGODB_URI")
                    + "\nMONGODB_DATABASE=" + getEnvKey("MONGODB_DATABASE")
                    + "\nMONGODB_COLLECTION=" + getEnvKey("MONGODB_COLLECTION"));
        } catch (IOException exception) {
            LOGGER.info("An error occurred during saving the .env: " + exception.getMessage());
        }
        DEFAULT_PREFIX = pre;
        Dotenv.load();
        LOGGER.info("All .env settings have been saved into the .env file");
    }

    public static void createENV() {
        File envFile = new File(".env");
        if (!envFile.exists()) {
            try (FileWriter writer = new FileWriter(".env")) {
                writer.write("TOKEN="
                        + "\nPRE=" + DEFAULT_PREFIX
                        + "\nOWNER="
                        + "\nKEY="
                        + "\nSHARD="
                        + "\nUSE_MONGODB=" + false
                        + "\nMONGODB_URI="
                        + "\nMONGODB_DATABASE="
                        + "\nMONGODB_COLLECTION=");
            } catch (IOException exception) {
                LOGGER.info("An error occurred during creating the .env: " + exception.getMessage());
            }
        }
    }

    public static void saveForAQuarter() {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(inventoryManager::saveInventory, 0, SAVE_INTERVAL, TimeUnit.MINUTES);
        LOGGER.info("Prefixes and languages are going to be saved for every " + SAVE_INTERVAL + " minutes");
    }

    public static void startBot(String apiKey, String token, int shards) throws LoginException {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();
        if (configurationProperties.isUseMongoDB()) {
            Inventory.setInventoryStrategy(new MongoDBStoreStrategy());
        } else {
            Inventory.setInventoryStrategy(new LocalStoreStrategy());
        }
        LanguageManager.getMessages();
        JDABuilder.createDefault(apiKey)
                .addEventListeners(new EventAdapter())
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

    public static double getCurrentVersion() {
        return versionController.getCurrentVersion();
    }

    public static String getDownloadURL() {
        return versionController.getDownloadURL();
    }

    public static boolean isLatestVersion() {
        return versionController.isLatestVersion();
    }

    public static void saveInventory() {
        if (!configurationProperties.isUseMongoDB())
            inventoryManager.saveInventory();
    }

    public static void loadInventory() {
        if (!configurationProperties.isUseMongoDB())
            inventoryManager.getInventory();
    }
}

package com.tzesh.tzebot.core.inventory.local;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

import static com.tzesh.tzebot.core.inventory.Inventory.GUILD_CHANNELS;

/**
 * This is a class responsible for managing the inventory of the bot
 * @author tzesh
 */
public class LocalInventoryManager {
    private final Logger LOGGER = LoggerFactory.getLogger(LocalInventoryManager.class);
    private final String DATABASE_PATH = "database.bin";

    public void saveInventory() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("database.bin"))) {
            out.writeObject(GUILD_CHANNELS);
            out.reset();
        } catch (IOException exception) {
            LOGGER.info("An error occurred during saving databases process...");
        }
        LOGGER.info("Prefixes and languages are saved into database.");
    }

    public void getInventory() {
        File file = new File(DATABASE_PATH);

        if (!file.exists()) {
            LOGGER.info("Database file not found.");
            return;
        }

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                GUILD_CHANNELS = (HashMap<Long, GuildChannel>) in.readObject();
            } catch (Exception exception) {
                LOGGER.info("An error occurred during process...");
            }
        }

        LOGGER.info("Database has been loaded.");
    }
}

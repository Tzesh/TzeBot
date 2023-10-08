package com.tzesh.tzebot.core.inventory;

import com.tzesh.tzebot.core.adapter.EventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.tzesh.tzebot.core.inventory.Inventory.*;
import static com.tzesh.tzebot.core.inventory.Inventory.INITIALIZED_MUSIC_CHANNELS;

/**
 * This is a class responsible for managing the inventory of the bot
 *
 * @author tzesh
 */
public class InventoryManager {
    private final Logger LOGGER = LoggerFactory.getLogger(EventAdapter.class);
    private final String DATABASE_PATH = "database.bin";

    public void saveInventory() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("database.bin"))) {
            Map<Long, String> prefixDB = PREFIXES;
            out.writeObject(prefixDB);
            Map<Long, String> languagesDB = LANGUAGES;
            out.writeObject(languagesDB);
            Map<Long, LinkedList<Long>> voterolesDB = VOTE_ROLE_CHANNELS;
            out.writeObject(voterolesDB);
            Map<Long, HashMap<Long, Long>> musicchannelsDB = EMOJI_CONTROLLED_MUSIC_CHANNELS;
            out.writeObject(musicchannelsDB);
            Map<Long, Integer> volumesDB = VOLUMES;
            out.writeObject(volumesDB);
            Map<Long, Long> channelcreatedDB = INITIALIZED_MUSIC_CHANNELS;
            out.writeObject(channelcreatedDB);
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
                PREFIXES = (Map<Long, String>) in.readObject();
                LANGUAGES = (Map<Long, String>) in.readObject();
                VOTE_ROLE_CHANNELS = (Map<Long, LinkedList<Long>>) in.readObject();
                EMOJI_CONTROLLED_MUSIC_CHANNELS = (Map<Long, HashMap<Long, Long>>) in.readObject();
                VOLUMES = (Map<Long, Integer>) in.readObject();
                INITIALIZED_MUSIC_CHANNELS = (Map<Long, Long>) in.readObject();
            } catch (Exception exception) {
                LOGGER.info("An error occurred during process...");
            }
        }

        LOGGER.info("Database has been loaded.");
    }
}

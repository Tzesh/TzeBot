package com.tzesh.tzebot.core.version;

import com.tzesh.tzebot.core.config.ConfigurationManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.tzesh.tzebot.core.common.CommonConstants.CURRENT_VERSION;

/**
 * This is a class responsible for managing the version of the bot
 * @author tzesh
 */
public class VersionController {
    private final double currentVersion = CURRENT_VERSION;
    private String downloadURL = "https://api.github.com/repos/tzesh/TzeBot/releases/latest"; // to download the latest release
    private String tagName;
    private Logger LOGGER = LoggerFactory.getLogger(VersionController.class);
    private boolean isLatestVersion = false;
    private Double latestVersion;

    public VersionController() {
        this.controlVersion();
    }

    private void controlVersion() {
        try {
            URL url = new URL(downloadURL);

            String json = readFromUrl(url);
            JSONObject release = new JSONObject(json);
            tagName = release.getString("tag_name").replace("v", "");
            downloadURL = release.getJSONArray("assets").getJSONObject(0).get("browser_download_url").toString();

            if (currentVersion * 1000 < Double.parseDouble(tagName) * 1000) {
                latestVersion = Double.parseDouble(tagName);
                LOGGER.info("You are currently using TzeBot v" + currentVersion);
                LOGGER.info("Please update your TzeBot to latest version by downloading TzeBot-" + tagName + ".zip from update button.");
                return;
            }

            latestVersion = currentVersion;
            this.isLatestVersion = true;
        } catch (Exception e) {
            LOGGER.info("An error occurred during the version control: " + e.getMessage());
        }
    }

    private String readFromUrl(URL url) {
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        } catch (IOException e) {
            LOGGER.info("An error occurred during reading from URL: " + e.getMessage());
        }

        return null;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public double getCurrentVersion() {
        return currentVersion;
    }
    public boolean isLatestVersion() {
        return isLatestVersion;
    }
}

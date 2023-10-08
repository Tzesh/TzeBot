package com.tzesh.tzebot.core.music.client;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.tzesh.tzebot.core.config.ConfigurationManager;
import com.tzesh.tzebot.core.music.client.abstracts.AbstractPlatformClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * A YouTube client to manage the YouTube API calls
 *
 * @author tzesh
 */
public class YouTubeClient extends AbstractPlatformClient<YouTube> {

    @Override
    protected String getAudioInformation(String input) throws IOException {
        List<SearchResult> results = client.search()
                .list(Collections.singletonList("id,snippet"))
                .setQ(input)
                .setMaxResults(1L)
                .setType(Collections.singletonList("video"))
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                .setKey(ConfigurationManager.getEnvKey("key"))
                .execute()
                .getItems();
        if (!results.isEmpty()) {
            String videoId = results.get(0).getId().getVideoId();
            return "https://www.youtube.com/watch?v=" + videoId;
        }

        return null;
    }

    @Override
    public void initializeClient() {
        try {
            client = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("TzeBot")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}

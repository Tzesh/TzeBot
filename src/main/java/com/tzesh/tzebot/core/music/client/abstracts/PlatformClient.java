package com.tzesh.tzebot.core.music.client.abstracts;

import java.io.IOException;

/**
 * A platform client to manage the API calls to the platform (YouTube, Spotify, etc.)
 * @author tzesh
 */
public interface PlatformClient {
    String searchAudio(String input) throws IOException;
}

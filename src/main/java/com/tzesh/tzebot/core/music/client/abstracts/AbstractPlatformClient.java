package com.tzesh.tzebot.core.music.client.abstracts;

import java.io.IOException;

/**
 * An abstract class to manage the platform clients (YouTube, Spotify, etc.) and their API calls
 * @author tzesh
 */
public abstract class AbstractPlatformClient<T> implements PlatformClient {
    protected T client;

    protected abstract String getAudioInformation(String input) throws IOException;

    protected boolean checkPreRequisites() {
        return client != null;
    }

    public abstract void initializeClient();

    public AbstractPlatformClient() {
        initializeClient();
    }

    @Override
    public String searchAudio(String input) {
        if (checkPreRequisites()) {
            try {
                return getAudioInformation(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

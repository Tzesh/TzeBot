package com.tzesh.tzebot.utils;

import java.util.concurrent.TimeUnit;

/**
 * This class contains some useful methods for formatting
 * @author tzesh
 */
public class FormatHelper {
    public static String formatTime(long timeInMilis) {
        final long hours = timeInMilis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMilis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMilis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatURL(String input, boolean playlist) {
        return playlist ? input.substring(0, input.indexOf("&")).replace("https://www.youtube.com/watch?v=", "") : input.replace("https://www.youtube.com/watch?v=", "");
    }
}

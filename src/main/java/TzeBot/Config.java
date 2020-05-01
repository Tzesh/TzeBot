package TzeBot;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();
    public static final Map<Long, String> PREFIXES = new HashMap<>();
    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
}

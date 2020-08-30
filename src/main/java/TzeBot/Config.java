package TzeBot;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();
    public static final Map<Long, String> PREFIXES = new HashMap<>();
    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
    public static void save(String token, String pre, String owner, String key, String shards) {
         try (FileWriter writer = new FileWriter(".env")) {
            writer.write("TOKEN=" + token +
                    "\nPRE=" + pre +
                    "\nOWNER=" + owner +
                    "\nKEY=" + key +
                    "\nSHARD=" + shards);
        } catch (IOException exception) {
            System.out.println("An error occured during saving the .env");
        }
         System.out.println("All .env settings have been saved.");
     }
}

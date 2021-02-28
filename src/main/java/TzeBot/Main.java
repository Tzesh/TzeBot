package TzeBot;

import TzeBot.essentials.Config;
import TzeBot.essentials.LanguageManager;
import TzeBot.essentials.Listener;
import TzeBot.gui.TzeGUI;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws LoginException, SQLException { // Database support will be added in future
        if (args.length == 1 && args[0].equals("-nogui")) {
            System.out.println("Starting TzeBot without GUI" +
                    "\nIf you want to safely close TzeBot without losing any kind of information then write 'quit' you can also save database by writing 'save'");
            startBot();
        }
        else if (args.length < 1) {
            new TzeGUI().start(); // All of the functions and required things will be called and operated in TzeGUI.java
        }
        else {
            System.out.println("There is only one argument that you can use, which is '-nogui'");
            return;
        }
    }

    public static void startBot() {
        Config.getDatabase();
        Config.saveForAQuarter();
        LanguageManager.getMessages();
        try {
            DefaultShardManagerBuilder.createDefault(Config.get("TOKEN"))
                    .setToken(Config.get("TOKEN"))
                    .addEventListeners(new Listener())
                    .setShardsTotal(Integer.parseInt(Config.get("SHARD")))
                    .setActivity(Activity.listening(".help"))
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES)
                    .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .disableIntents(GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_INVITES)
                    .setStatus(OnlineStatus.ONLINE)
                    .setGatewayEncoding(GatewayEncoding.ETF)
                    .setAutoReconnect(true)
                    .setLargeThreshold(50)
                    .build();
        } catch (LoginException exception) {
            System.out.println("An error occurred please make sure you have set all variables properly and you have a sufficient internet connection.");
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.nextLine().toLowerCase().equals("save")) Config.saveDatabase();
            if (scanner.nextLine().toLowerCase().equals("quit")) {
                Config.saveDatabase();
                System.exit(0);
            }
            else System.out.println("Invalid command, commands are 'quit' and 'save'");
        }
    }
}

package TzeBot;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private Main() throws LoginException, SQLException {
        int shards = 1;
        boolean loop = true;

        Scanner scanner = new Scanner(System.in);
        System.out.println("TzeBot - Discord Bot for Public Use\n" +
                "github.com/Tzesh/TzeBot\n" +
                "Please be ensure that you have been done these things before using TzeBot:\n" +
                "1: In the same folder of TzeBot-1.0.jar there must be a .env file that contains data fields of bot used to run.\n" +
                "2: Content of .env file has to be as exactly same as this: \n" +
                "TOKEN='CONTAINS DISCORD BOT TOKEN'\n" +
                "PRE='DEFAULT PREFIX'\n" +
                "OWNER='DISCORD ID OF OWNER'\n" +
                "KEY='YOUTUBE API KEY'\n" +
                "If you've done these things you are one step far from to use TzeBot.\n" +
                "Aren't you not planning to run TzeBot too many servers 2 shards will be the most effective usage.");
        while (loop) {
            try {
                System.out.println("How many shards do you want to set?");
                int input = scanner.nextInt();
                if (input <= 0) {
                    System.out.println("Shard number must be bigger than 0.");
                } else {
                    shards = input;
                    loop = false;
                }
            } catch (InputMismatchException exception) {
                System.out.println("Please enter an integer value bigger than 0.");
                scanner.nextLine();
            }
        }

        DefaultShardManagerBuilder.createDefault(Config.get("key"))
                .setToken(Config.get("token"))
                .addEventListeners(new Listener())
                .setShardsTotal(shards)
                .build();

    }

    public static void main(String[] args) throws LoginException, SQLException {
        new Main();
    }
}

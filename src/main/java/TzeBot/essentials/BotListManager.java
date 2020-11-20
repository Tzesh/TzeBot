package TzeBot.essentials;

import net.dv8tion.jda.api.JDA;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.List;

public class BotListManager {
    DiscordBotListAPI api;

    BotListManager(String token, String botId) {
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(token)
                .botId(botId)
                .build();
        this.api = api;
    }

    void setStats(JDA jda) {
        List<JDA> shards = jda.getShardManager().getShards();
        List<Integer> shardServerCounts = null;
        shards.forEach(shard -> {
            shardServerCounts.add(jda.getGuilds().size());
        });
        api.setStats(shardServerCounts);
    }
}

package TzeBot.essentials;

import net.dv8tion.jda.api.JDA;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class BotListManager {
    DiscordBotListAPI api;

    BotListManager() {
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token(Config.dblToken)
                .botId(Config.botId)
                .build();
        this.api = api;
    }

    void setStats(JDA jda) {
        List<JDA> shards = Objects.requireNonNull(jda.getShardManager()).getShards();
        List<Integer> shardServerCounts = new LinkedList<>();
        shards.forEach(shard -> {
            shardServerCounts.add(jda.getGuilds().size());
        });
        api.setStats(shardServerCounts);
    }

    public boolean checkVote(String userID) {
        AtomicBoolean isVoted = new AtomicBoolean(false);
        api.hasVoted(userID).whenComplete((hasVoted, e) -> {
            if (hasVoted) isVoted.set(true);
            else isVoted.set(false);
        });
        return isVoted.get();
    }
}

package com.tzesh.tzebot.core.channel.impl;

import com.tzesh.tzebot.core.channel.abstracts.AbstractGuildChannel;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

/**
 * This is a simple class for guild channels
 * @author tzesh
 */
public class GuildChannelImpl extends AbstractGuildChannel {
    private static final Long serialVersionUID = 1L;

    @BsonCreator
    public GuildChannelImpl(@BsonProperty("guild_id") Long guildID,
                            @BsonProperty("bounded_music_channel_id") Long boundedMusicChannelID,
                            @BsonProperty("music_channel_message_id") Long musicChannelMessageID,
                            @BsonProperty("vote_role_message_id") Long voteRoleMessageID,
                            @BsonProperty("vote_role_ids") List<Long> voteRoleIDs,
                            @BsonProperty("volume") int volume,
                            @BsonProperty("prefix") String prefix,
                            @BsonProperty("language") String language) {
        super(guildID, boundedMusicChannelID, musicChannelMessageID, voteRoleMessageID, voteRoleIDs, volume, prefix, language);
    }

    public GuildChannelImpl(Long guildID) {
        super(guildID);
    }
}

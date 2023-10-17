package com.tzesh.tzebot.core.channel.abstracts;

import com.google.gson.Gson;
import com.tzesh.tzebot.core.common.CommonConstants;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.core.music.constants.MusicCommonConstants;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * This is a simple abstract class for channel types
 * @author tzesh
 */
public abstract class AbstractGuildChannel implements GuildChannel, Serializable {
    private static final Long serialVersionUID = 1L;
    @BsonProperty("guild_id")
    private final Long guildID;
    @BsonProperty("bounded_music_channel_id")
    private Long boundedMusicChannelID;
    @BsonProperty("music_channel_message_id")
    private Long musicChannelMessageID;
    @BsonProperty("vote_role_message_id")
    private Long voteRoleMessageID;
    @BsonProperty("vote_role_ids")
    private List<Long> voteRoleIDs;
    @BsonProperty("volume")
    private int volume;
    @BsonProperty("prefix")
    private String prefix;
    @BsonProperty("language")
    private String language;

    @BsonCreator
    public AbstractGuildChannel(@BsonProperty("guild_id") Long guildID,
                                @BsonProperty("bounded_music_channel_id") Long boundedMusicChannelID,
                                @BsonProperty("music_channel_message_id") Long musicChannelMessageID,
                                @BsonProperty("vote_role_message_id") Long voteRoleMessageID,
                                @BsonProperty("vote_role_ids") List<Long> voteRoleIDs,
                                @BsonProperty("volume") int volume,
                                @BsonProperty("prefix") String prefix,
                                @BsonProperty("language") String language) {
        this.guildID = guildID;
        this.boundedMusicChannelID = boundedMusicChannelID;
        this.musicChannelMessageID = musicChannelMessageID;
        this.voteRoleMessageID = voteRoleMessageID;
        this.voteRoleIDs = voteRoleIDs;
        this.volume = volume;
        this.prefix = prefix;
        this.language = language;
    }

    public AbstractGuildChannel(Long guildID) {
        this.guildID = guildID;
        this.boundedMusicChannelID = null;
        this.musicChannelMessageID = null;
        this.voteRoleMessageID = null;
        this.voteRoleIDs = null;
        this.volume = MusicCommonConstants.DEFAULT_VOLUME;
        this.prefix = CommonConstants.DEFAULT_PREFIX;
        this.language = CommonConstants.DEFAULT_LANGUAGE;
    }

    @Override
    public String getJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean doesMusicChannelExist() {
        return this.boundedMusicChannelID != null;
    }

    @Override
    public boolean doesVoteRoleChannelExist() {
        return this.voteRoleMessageID != null;
    }

    @Override
    public void save() {
        Inventory.save(this);
    }

    @Override
    public Long getGuildID() {
        return this.guildID;
    }

    @Override
    public Long getBoundedMusicChannelID() {
        return this.boundedMusicChannelID;
    }

    @Override
    public Long getMusicChannelMessageID() {
        return this.musicChannelMessageID;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public Long getVoteRoleMessageID() {
        return this.voteRoleMessageID;
    }

    @Override
    public List<Long> getVoteRoleIDs() {
        return this.voteRoleIDs;
    }

    @Override
    public int getVolume() {
        return this.volume;
    }

    @Override
    public void setBoundedMusicChannelID(Long boundedMusicChannelID) {
        this.boundedMusicChannelID = boundedMusicChannelID;
    }

    @Override
    public void setMusicChannelMessageID(Long musicChannelMessageID) {
        this.musicChannelMessageID = musicChannelMessageID;
    }

    @Override
    public void setVoteRoleMessageID(Long voteRoleMessageID) {
        this.voteRoleMessageID = voteRoleMessageID;
    }

    @Override
    public void setVoteRoleIDs(List<Long> voteRoleIDs) {
        this.voteRoleIDs = voteRoleIDs;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + guildID +
                ", boundedMusicChannelID=" + boundedMusicChannelID +
                ", musicChannelMessageID=" + musicChannelMessageID +
                ", voteRoleMessageID=" + voteRoleMessageID +
                ", voteRoleIDs=" + voteRoleIDs +
                ", volume=" + volume +
                ", prefix='" + prefix + '\'' +
                ", language='" + language + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return this.guildID.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractGuildChannel) {
            AbstractGuildChannel other = (AbstractGuildChannel) obj;
            return this.guildID.equals(other.guildID);
        }

        return false;
    }
}

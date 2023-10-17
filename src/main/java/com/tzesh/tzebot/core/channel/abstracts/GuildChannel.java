package com.tzesh.tzebot.core.channel.abstracts;

import java.io.Serializable;
import java.util.List;

/**
 * This is a simple interface for channel types
 *
 * @author tzesh
 */
public interface GuildChannel extends Serializable {
    Long getGuildID();

    Long getBoundedMusicChannelID();

    Long getMusicChannelMessageID();

    String getPrefix();

    String getLanguage();

    String getJson();

    Long getVoteRoleMessageID();

    List<Long> getVoteRoleIDs();
    int getVolume();

    boolean doesMusicChannelExist();

    boolean doesVoteRoleChannelExist();

    void setBoundedMusicChannelID(Long boundedMusicChannelID);

    void setMusicChannelMessageID(Long musicChannelMessageID);

    void setVoteRoleMessageID(Long voteRoleMessageID);

    void setVoteRoleIDs(List<Long> voteRoleIDs);

    void setPrefix(String prefix);

    void setLanguage(String language);
    void setVolume(int volume);
    void save();
}

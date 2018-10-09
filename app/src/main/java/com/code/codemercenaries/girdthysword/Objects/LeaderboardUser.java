package com.code.codemercenaries.girdthysword.Objects;

/**
 * Created by Joel Kingsley on 04-06-2018.
 */

public class LeaderboardUser {

    int rank;
    String profileURL;
    String displayName;
    int level;
    String status;
    Long versesMemorized;

    public LeaderboardUser(int rank, String profileURL, String displayName, int level, String status, Long versesMemorized) {
        this.rank = rank;
        this.profileURL = profileURL;
        this.displayName = displayName;
        this.level = level;
        this.status = status;
        this.versesMemorized = versesMemorized;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersesMemorized() {
        return versesMemorized;
    }

    public void setVersesMemorized(Long versesMemorized) {
        this.versesMemorized = versesMemorized;
    }

}

package com.example.textbookmarketplace.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_listings")
public class UserListing {
    @PrimaryKey
    @NonNull
    private String id;

    private String textbookId;
    private String userId;
    private boolean isFavorite;
    private long timestamp;

    public UserListing() {
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.isFavorite = false;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getTextbookId() { return textbookId; }
    public void setTextbookId(String textbookId) { this.textbookId = textbookId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
package com.udacity.luisev96.popularmovies.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseReview {

    @PrimaryKey
    @NonNull
    private String id;
    private int typeId;
    private String author;
    private String content;
    private String url;

    public DatabaseReview(@NonNull String id, String author, String content, String url, int typeId) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
        this.typeId = typeId;
    }

    public String getId() {
        return id;
    }

    int getTypeId() { return typeId; }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}

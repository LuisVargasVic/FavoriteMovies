package com.udacity.luisev96.popularmovies.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DatabaseVideo {

    @PrimaryKey
    @NonNull
    private String id;
    private int typeId;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public DatabaseVideo(@NonNull String id, String key, String name, String site, int size, String type, int typeId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.typeId = typeId;
    }

    public String getId() {
        return id;
    }

    int getTypeId() { return typeId; }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

}

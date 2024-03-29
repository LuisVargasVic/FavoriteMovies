package com.udacity.luisev96.popularmovies.domain;

import java.io.Serializable;

public class Video implements Serializable {
    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Video(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

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

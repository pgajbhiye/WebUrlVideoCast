package com.orion.videourlcast;

import java.util.ArrayList;

class VideoMetaData {

    private String title;
    private String subtitle;
    private String thumb;
    private ArrayList<String> sources;
    private String description;

    public VideoMetaData(String title, String subtitle, String thumb, ArrayList<String> sources, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.thumb = thumb;
        this.sources = sources;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getThumb() {
        return thumb;
    }

    public ArrayList<String> getSources() {
        return sources;
    }

    public String getDescription() {
        return description;
    }
}

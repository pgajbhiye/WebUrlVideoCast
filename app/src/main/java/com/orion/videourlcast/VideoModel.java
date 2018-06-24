package com.orion.videourlcast;

import java.util.ArrayList;

public class VideoModel {
    String genreName;
    ArrayList<VideoMetaData> videosMetaData;


    public VideoModel(String genreName, ArrayList<VideoMetaData> videosMetaData) {
        this.genreName = genreName;
        this.videosMetaData = videosMetaData;
    }

    public String getGenreName() {
        return genreName;
    }

    public ArrayList<VideoMetaData> getVideosMetaData() {
        return videosMetaData;
    }
}
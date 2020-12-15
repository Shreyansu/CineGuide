package com.shreyansu.chillout.videos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse
{
    @SerializedName("id")
    private String id;

    @SerializedName("results")
    private List<Video> videos;

    public VideoResponse(String id, List<Video> videos) {
        this.id = id;
        this.videos = videos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}

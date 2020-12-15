package com.shreyansu.chillout.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class genreList
{
    @SerializedName("genres")
    private  List<Genre> genres;

    public genreList(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres)
    {
        this.genres = genres;
    }
}

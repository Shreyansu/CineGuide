package com.shreyansu.chillout.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCastPersonResponse
{
    @SerializedName("id")
    private Integer id;
    @SerializedName("cast")
    private List<MoviePersonCast> casts;

    public MovieCastPersonResponse(Integer id, List<MoviePersonCast> casts) {
        this.id = id;
        this.casts = casts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MoviePersonCast> getCasts() {
        return casts;
    }

    public void setCasts(List<MoviePersonCast> casts) {
        this.casts = casts;
    }
}

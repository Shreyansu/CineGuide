package com.shreyansu.chillout.network.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditMovieResponse
{
    @SerializedName("id")
    private Integer id;

    @SerializedName("cast")
    private List<MovieDetail> casts;

    @SerializedName("crew")
    private List<CrewMovieDetail> crews;

    public CreditMovieResponse(Integer id, List<MovieDetail> casts, List<CrewMovieDetail> crews) {
        this.id = id;
        this.casts = casts;
        this.crews = crews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieDetail> getCasts() {
        return casts;
    }

    public void setCasts(List<MovieDetail> casts) {
        this.casts = casts;
    }

    public List<CrewMovieDetail> getCrews() {
        return crews;
    }

    public void setCrews(List<CrewMovieDetail> crews)
    {
        this.crews = crews;
    }


}

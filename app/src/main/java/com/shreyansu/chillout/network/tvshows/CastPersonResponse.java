package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastPersonResponse
{
    @SerializedName("cast")
    private List<CastPerson> casts;
    @SerializedName("id")
    private Integer id;

    public CastPersonResponse(List<CastPerson> casts, Integer id) {
        this.casts = casts;
        this.id = id;
    }

    public List<CastPerson> getCasts() {
        return casts;
    }

    public void setCasts(List<CastPerson> casts) {
        this.casts = casts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowCreditResponse
{
    @SerializedName("id")
    private Integer id;
    @SerializedName("cast")
    private List<CastDetail> casts;

    @SerializedName("crew")
    private List<CrewDetail> crews;

    public ShowCreditResponse(Integer id, List<CastDetail> casts, List<CrewDetail> crews) {
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

    public List<CastDetail> getCasts() {
        return casts;
    }

    public void setCasts(List<CastDetail> casts) {
        this.casts = casts;
    }

    public List<CrewDetail> getCrews() {
        return crews;
    }

    public void setCrews(List<CrewDetail> crews) {
        this.crews = crews;
    }

}

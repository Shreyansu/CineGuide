package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

public class CastDetail
{
    @SerializedName("name")
    private String name;
    @SerializedName("character")
    private String character;
    @SerializedName("order")
    private Integer order;
    @SerializedName("profile_path")
    private String profilePath;
    @SerializedName("credit_id")
    private String creditId;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("id")
    private Integer id;


    public CastDetail(String name, String character, Integer order, String profilePath, String creditId, Integer gender, Integer id) {
        this.name = name;
        this.character = character;
        this.order = order;
        this.profilePath = profilePath;
        this.creditId = creditId;
        this.gender = gender;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

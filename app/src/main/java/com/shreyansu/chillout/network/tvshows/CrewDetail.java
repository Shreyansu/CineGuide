package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

public class CrewDetail
{
    @SerializedName("credit_id")
    private String creditId;
    @SerializedName("id")
    private Integer id;
    @SerializedName("department")
    private String department;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("job")
    private String job;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profilePath;

    public CrewDetail(String creditId, Integer id, String department, Integer gender, String job, String name, String profilePath) {
        this.creditId = creditId;
        this.id = id;
        this.department = department;
        this.gender = gender;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}

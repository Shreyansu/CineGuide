package com.shreyansu.chillout.network.tvshows;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowDetail
{
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("id")
    private Integer id;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("origin_country")
    private List<String> originCountries;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("name")
    private String name;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    public ShowDetail(String originalName, Integer id, String firstAirDate, Double popularity, Integer voteCount, Double voteAverage, String posterPath, String originalLanguage, List<String> originCountries, String backdropPath, String overview, String name, List<Integer> genreIds) {
        this.originalName = originalName;
        this.id = id;
        this.firstAirDate = firstAirDate;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originCountries = originCountries;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.name = name;
        this.genreIds = genreIds;
    }

}

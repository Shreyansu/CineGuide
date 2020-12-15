package com.shreyansu.chillout.network;

import android.graphics.Movie;

import com.shreyansu.chillout.network.movies.NowShowingMoviesResponse;
import com.shreyansu.chillout.network.movies.PopularMoviesResponse;
import com.shreyansu.chillout.network.movies.TopRatedMoviesResponse;
import com.shreyansu.chillout.network.movies.upcomingMoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface
{

    @GET("movie/now_playing")
    Call<NowShowingMoviesResponse> getNowShowingResponse(@Query("api_key") String apiKey,@Query("page") String page,@Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovieResponse(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);

    @GET("movie/upcoming")
    Call<upcomingMoviesResponse> getUpcomingMovieResponse(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovieResponse(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    //TODO 16-12







}

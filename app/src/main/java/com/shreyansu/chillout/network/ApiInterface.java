package com.shreyansu.chillout.network;



import android.provider.CalendarContract;

import androidx.annotation.IntegerRes;

import com.shreyansu.chillout.network.movies.CreditMovieResponse;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.NowShowingMoviesResponse;
import com.shreyansu.chillout.network.movies.PopularMoviesResponse;
import com.shreyansu.chillout.network.movies.SimilarMovieResponse;
import com.shreyansu.chillout.network.movies.TopRatedMoviesResponse;
import com.shreyansu.chillout.network.movies.upcomingMoviesResponse;
import com.shreyansu.chillout.network.tvshows.ShowAirtodayResponse;
import com.shreyansu.chillout.network.tvshows.ShowOnAirResponse;
import com.shreyansu.chillout.network.tvshows.ShowPopularResponse;
import com.shreyansu.chillout.network.tvshows.ShowTopRatedResponse;
import com.shreyansu.chillout.network.videos.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface
{

    @GET("movie/upcoming")
    Call<upcomingMoviesResponse> getUpcomingMovieResponse(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovieResponse(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/now_playing")
    Call<NowShowingMoviesResponse> getNowShowingResponse(@Query("api_key") String apiKey,@Query("page") String page,@Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovieResponse(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);


    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getMovieVideos(@Path("id") Integer movieId,@Query("api_key") String apiKey);

    @GET("movie/{id}/credita")
    Call<CreditMovieResponse> getMovieCredits(@Path("id") Integer movieId,@Query("api_key") String apiKey);

    @GET("movie/{id}/similar")
    Call<SimilarMovieResponse> getSimilarMovies(@Path("id") Integer movieId,@Query("api_key") String apiKey,@Query("page") Integer page);

    @GET("genre/movie/list")
    Call<com.shreyansu.chillout.network.movies.genreList> getMovieGenreList(@Query("api_key") String apiKey);


    //Shows
    @GET("tv/airing_today")
    Call<ShowAirtodayResponse> getAiringTodayShows(@Query("api_key") String apiKey,@Query("page") Integer page);

    @GET("tv/on_the_air")
    Call<ShowOnAirResponse> getOnAirShows(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("tv/popular")
    Call<ShowPopularResponse> getPopularShows(@Query("api_key") String apiKey,@Query("page") Integer page);

    @GET("tv/top_rated")
    Call<ShowTopRatedResponse> getTopRatedResponse(@Query("api_key") String apiKey,@Query("page") String page);









}

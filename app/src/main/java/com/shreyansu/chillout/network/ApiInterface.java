package com.shreyansu.chillout.network;
import android.provider.CalendarContract;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringDef;

import com.shreyansu.chillout.network.movies.CreditMovieResponse;
import com.shreyansu.chillout.network.movies.Movie;
import com.shreyansu.chillout.network.movies.MovieCastPersonResponse;
import com.shreyansu.chillout.network.movies.NowShowingMoviesResponse;
import com.shreyansu.chillout.network.movies.PopularMoviesResponse;
import com.shreyansu.chillout.network.movies.SimilarMovieResponse;
import com.shreyansu.chillout.network.movies.TopRatedMoviesResponse;
import com.shreyansu.chillout.network.movies.upcomingMoviesResponse;
import com.shreyansu.chillout.network.tvshows.CastPersonResponse;
import com.shreyansu.chillout.network.tvshows.ShowAirtodayResponse;
import com.shreyansu.chillout.network.tvshows.ShowCreditResponse;
import com.shreyansu.chillout.network.tvshows.ShowOnAirResponse;
import com.shreyansu.chillout.network.tvshows.ShowPopularResponse;
import com.shreyansu.chillout.network.tvshows.ShowTopRatedResponse;
import com.shreyansu.chillout.network.tvshows.Shows;
import com.shreyansu.chillout.network.tvshows.SimilarShowResponse;
import com.shreyansu.chillout.network.videos.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface
{

    @GET("movie/upcoming")
    Call<upcomingMoviesResponse> getUpcomingMovie(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);

    @GET("movie/top_rated")
    Call<TopRatedMoviesResponse> getTopRatedMovie(@Query("api_key") String apiKey, @Query("page") Integer page, @Query("region") String region);

    @GET("movie/now_playing")
    Call<NowShowingMoviesResponse> getNowShowing(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);

    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovie(@Query("api_key") String apiKey,@Query("page") Integer page,@Query("region") String region);


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

    @GET("tv/{id}")
    Call<Shows> getShowsDetails(@Path("id") Integer ShowId,@Query("api_key") String apiKey);

    @GET("tv/{id}/videos")
    Call<VideoResponse> getShowsVideos(@Path("id") Integer movieId,@Query("api_key") String apiKey);

    @GET("tv/{id}/credits")
    Call<ShowCreditResponse> getShowCredits(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/similar")
    Call<SimilarShowResponse> getSimilarShows(@Path("id") Integer movieId, @Query("api_key") String apiKey, @Query("page") Integer page);


    @GET("genre/tv/list")
    Call<com.shreyansu.chillout.network.tvshows.genreList> getShowsList(@Query("api_key") String apiKey);


    //person

    @GET("person/{id}")
    Call<Actor> getActorDetail(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/movie_credits")
    Call<MovieCastPersonResponse> getMovieCastPerson(@Path("id") Integer personId, @Query("api_key") String apiKey);

    @GET("person/{id}/tv_credits")
    Call<CastPersonResponse> getShowsCastpersonResponse(@Path("id") Integer personId,@Query("api_key") String apiKey);

}

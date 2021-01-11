package com.shreyansu.chillout.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shreyansu.chillout.Database.DatabaseHelper;
import com.shreyansu.chillout.network.movies.MovieDetail;
import com.shreyansu.chillout.network.tvshows.ShowDetail;

import java.util.ArrayList;
import java.util.List;

public class Favourite
{
    public static void addMovieToFav(Context context,Integer movieId,String posterPath,String name)
    {
        if(movieId==null)
            return;
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if(!isFavMovie(context,movieId))
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID,movieId);
            contentValues.put(DatabaseHelper.POSTER_PATH,posterPath);
            contentValues.put(DatabaseHelper.NAME,name);

            database.insert((DatabaseHelper.FAVOURITE_MOVIE_TABLE_NAME),null,contentValues);
        }
        database.close();
    }

    public static boolean isFavMovie(Context context, Integer movieId)
    {
        boolean isFavMovie;
        if(movieId==null)
            return false;
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        SQLiteDatabase database=databaseHelper.getReadableDatabase();

        Cursor cursor=database.query(DatabaseHelper.FAVOURITE_MOVIE_TABLE_NAME,null,DatabaseHelper.MOVIE_ID + " = " + movieId,null,null,null,null);
        if(cursor.getCount()==1)
            isFavMovie=true;
        else
            isFavMovie=false;

        cursor.close();
        database.close();
        return isFavMovie;
    }
    public static void removeMoviefromfavourite(Context context,Integer movieId)
    {
        if(movieId==null)
            return ;
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        SQLiteDatabase database=databaseHelper.getWritableDatabase();
        if(isFavMovie(context,movieId))
        {
            database.delete(DatabaseHelper.FAVOURITE_MOVIE_TABLE_NAME,DatabaseHelper.MOVIE_ID + " = " + movieId,null);

        }
        database.close();
    }

    public static void addTVShowsToFav(Context context,Integer showID,String posterPath,String name)
    {
        if(showID==null)
            return;
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if(!isFavMovie(context,showID))
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put(DatabaseHelper.TV_SHOW_ID,showID);
            contentValues.put(DatabaseHelper.POSTER_PATH,posterPath);
            contentValues.put(DatabaseHelper.NAME,name);

            database.insert((DatabaseHelper.FAVOURITE_TV_SHOWS_NAME),null,contentValues);
        }
        database.close();
    }
    public static boolean isTVShowFav(Context context, Integer tvShowId) {
        if (tvShowId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isTVShowFav;
        Cursor cursor = database.query(DatabaseHelper.FAVOURITE_TV_SHOWS_NAME, null, DatabaseHelper.TV_SHOW_ID + " = " + tvShowId, null, null, null, null);
        if (cursor.getCount() == 1)
            isTVShowFav = true;
        else
            isTVShowFav = false;

        cursor.close();
        database.close();
        return isTVShowFav;
    }
    public static void removeTVShowsfromfavourite(Context context,Integer showId)
    {
        if(showId==null)
            return ;
        DatabaseHelper databaseHelper=new DatabaseHelper(context);
        SQLiteDatabase database=databaseHelper.getWritableDatabase();
        if(isFavMovie(context,showId))
        {
            database.delete(DatabaseHelper.FAVOURITE_TV_SHOWS_NAME,DatabaseHelper.TV_SHOW_ID+ " = " + showId,null);

        }
        database.close();
    }

    public static List<MovieDetail> getFavMovieDetails(Context context)
    {
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        SQLiteDatabase database =databaseHelper.getReadableDatabase();

        List<MovieDetail> favMovies =new ArrayList<>();
        Cursor  cursor = database.query(DatabaseHelper.FAVOURITE_MOVIE_TABLE_NAME,null,null,null,null,null,DatabaseHelper.ID +" DESC");
        while(cursor.moveToNext())
        {
            int movieId=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
            String posterPath=cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER_PATH));
            String name=cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            favMovies.add(new MovieDetail(null,movieId,null,null, name,null,posterPath,null,null,null,null,null,null,null));

        }
        cursor.close();
        database.close();
        return favMovies;
    }

    public static List<ShowDetail> getFavTVShowDetails(Context context)
    {
        DatabaseHelper databaseHelper =new DatabaseHelper(context);
        SQLiteDatabase database =databaseHelper.getReadableDatabase();

        List<ShowDetail> favTVShows =new ArrayList<>();
        Cursor  cursor = database.query(DatabaseHelper.FAVOURITE_TV_SHOWS_NAME,null,null,null,null,null,DatabaseHelper.ID +" DESC");
        while(cursor.moveToNext())
        {
            int showId=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TV_SHOW_ID));
            String posterPath=cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER_PATH));
            String name=cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            favTVShows.add(new ShowDetail(null,showId,name,null,null,null,posterPath,null,null,null,null,null,null));

        }
        cursor.close();
        database.close();
        return favTVShows;
    }
}

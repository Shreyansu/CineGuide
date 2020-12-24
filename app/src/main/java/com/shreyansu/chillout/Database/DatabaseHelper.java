package com.shreyansu.chillout.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.mbms.StreamingServiceInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME="database.db";
    public static final String FAVOURITE_MOVIE_TABLE_NAME="FavouriteMoviesTable";
    public static final String FAVOURITE_TV_SHOWS_NAME="FavouriteTVShowstable";
    public static final String ID="id";
    public static final String MOVIE_ID="movie_id";
    public static final String TV_SHOW_ID="tv_show_id";
    public static final String POSTER_PATH="poster_path";
    public static final String NAME="name";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String queryCreateMovieTable= "CREATE TABLE " + FAVOURITE_MOVIE_TABLE_NAME +" ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " INTEGER, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT )";

        String queryCreateTVShowsTale = "CREATE TABLE " + FAVOURITE_TV_SHOWS_NAME +" ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " INTEGER, "
                + POSTER_PATH + " TEXT, "
                + NAME + " TEXT )";
        sqLiteDatabase.execSQL(queryCreateMovieTable);
        sqLiteDatabase.execSQL(queryCreateTVShowsTale);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.shreyansu.chillout.util;

import com.shreyansu.chillout.network.movies.Genre;

import java.util.HashMap;
import java.util.List;

public class MovieGenre
{
    private static HashMap<Integer,String> Mpgenre;

    public static boolean isgenreLoaded()
    {
        return (Mpgenre !=null);
    }

    public static void loadGenreList(List<Genre> genres)
    {
        if(genres==null)
            return;
        Mpgenre=new HashMap<>();
        for(Genre g:genres)
        {
            Mpgenre.put(g.getId(),g.getGenreName());
        }
    }

    public static String getGenreName(Integer genreId)
    {
        if(genreId==null)
            return null;
        return Mpgenre.get(genreId);
    }
}

package com.shreyansu.chillout.util;

import com.shreyansu.chillout.network.tvshows.Genre;

import java.util.HashMap;
import java.util.List;

public class ShowGenre
{
    private static HashMap<Integer,String> genreMap;

    public static boolean isGenreListLoaded()
    {
        return genreMap!=null;
    }
    public static void loadGenre(List<Genre> genres)
    {
        if(genres ==null)
            return;
        genreMap=new HashMap<>();
        for(Genre g :genres)
        {
            genreMap.put(g.getId(),g.getGenreName());
        }
    }
    public static String getGenreName(Integer genreId)
    {
        if(genreId==null)
            return null;
        return genreMap.get(genreId);
    }
}

package com.shreyansu.chillout.network.search;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.shreyansu.chillout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchLoader extends AsyncTaskLoader<SearchResponse>
{
    private Context context;
    private String kQuery;
    private String kPage;

    public SearchLoader(@NonNull Context context, String kQuery, String kPage)
    {

        super(context);
        this.context = context;
        this.kQuery = kQuery;
        this.kPage = kPage;
    }

    @Nullable
    @Override
    public SearchResponse loadInBackground()
    {
        try
        {
            String urlString="https://api.themoviedb.org/3/" +
                    "search/multi" + "?" +
                    "api_key=" + context.getResources().getString(R.string.MOVIE_DB_API_KEY)
                    + "&"
                    + "query=" + kQuery
                    + "&"
                    + "page=" + kPage;

            URL url=new URL(urlString);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() !=200)
                return null;
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner sc=new Scanner(inputStream);
            String jsonString ="";
            while(sc.hasNext())
            {
                jsonString+=sc.nextLine();
            }
            JSONObject searchJsonObject = new JSONObject(jsonString);
            SearchResponse searchResponse =new SearchResponse();
            searchResponse.setPage(searchJsonObject.getInt("page"));
            JSONArray resultJsonArray= (searchJsonObject.getJSONArray("results"));
            searchResponse.setTotalPages(searchJsonObject.getInt("total_pages"));

            List<SearchResult> result =new ArrayList<>();
            for(int i=0;i<resultJsonArray.length();i++)
            {
                JSONObject res=(JSONObject)resultJsonArray.get(i);
                SearchResult searchResult=new SearchResult();
                switch (res.getString("media_type"))
                {
                    case "movie":
                        searchResult.setId(res.getInt("id"));
                        searchResult.setName(res.getString("title"));
                        searchResult.setPosterPath(res.getString("overview"));
                        searchResult.setMediaType("movie");
                        searchResult.setReleaseDate(res.getString("release_date"));
                        searchResult.setPosterPath(res.getString("poster_path"));
                        break;

                    case "tv":
                        searchResult.setId(res.getInt("id"));
                        searchResult.setName(res.getString("name"));
                        searchResult.setPosterPath(res.getString("overview"));
                        searchResult.setMediaType("tv");
                        searchResult.setReleaseDate(res.getString("release_date"));
                        searchResult.setPosterPath(res.getString("first_air_date"));
                        break;

                    case "person":
                        searchResult.setId(res.getInt("id"));
                        searchResult.setName(res.getString("name"));
                        searchResult.setPosterPath(null);
                        searchResult.setMediaType(res.getString("person"));
                        searchResult.setReleaseDate(null);
                        searchResult.setPosterPath(res.getString("profile_path"));
                        break;

                }
                result.add(searchResult);
            }
            searchResponse.setResults(result);


        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package com.dtavana.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String posterPath;
    String backgroundPath;
    String title;
    String overview;

    public Movie(JSONObject obj) throws JSONException {
        this.posterPath = obj.getString("poster_path");
        this.title = obj.getString("title");
        this.overview = obj.getString("overview");
        this.backgroundPath = obj.getString("backdrop_path");
    }

    public static List<Movie> fromJsonArray(JSONArray arr) throws JSONException {
        List<Movie> res = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++) {
            res.add(new Movie(arr.getJSONObject(i)));
        }
        return res;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getBackgroundPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backgroundPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}

package com.dtavana.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String posterPath;
    String backgroundPath;
    String title;
    String overview;
    Double voteAverage;
    Integer id;
    String releaseDate;
    Integer voteCount;

    public String videoId;

    String size;
    String imageBaseUrl;

    public Movie() {}

    public Movie(JSONObject obj, String size, String imageBaseUrl) throws JSONException {
        this.posterPath = obj.getString("poster_path");
        this.title = obj.getString("title");
        this.overview = obj.getString("overview");
        this.backgroundPath = obj.getString("backdrop_path");
        this.voteAverage = obj.getDouble("vote_average");
        this.id = obj.getInt("id");
        this.releaseDate = obj.getString("release_date");
        this.voteCount = obj.getInt("vote_count");

        this.size = size;
        this.imageBaseUrl = imageBaseUrl;
    }

    public static List<Movie> fromJsonArray(JSONArray arr, String size, String imageBaseUrl) throws JSONException {
        List<Movie> res = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++) {
            res.add(new Movie(arr.getJSONObject(i), size, imageBaseUrl));
        }
        return res;
    }

    public String getPosterPath() {
        return String.format("%s%s%s", imageBaseUrl, size, posterPath);
    }

    public String getBackgroundPath() {
        return String.format("%s%s%s", imageBaseUrl, size, backgroundPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getId() {
        return id;
    }

    public String getReleaseDate() {
        return String.format("Released: %s", releaseDate.replace('-', '/'));
    }

    public String getVoteCount() {
        return String.format("Vote Count: %d", voteCount);
    }
}

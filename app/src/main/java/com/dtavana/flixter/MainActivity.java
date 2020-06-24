package com.dtavana.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.dtavana.flixter.adapters.MovieAdapter;
import com.dtavana.flixter.databinding.ActivityMainBinding;
import com.dtavana.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing";
    public static final String CONFIG_URL = "https://api.themoviedb.org/3/configuration";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movies = new ArrayList<>();

        // Create adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        // Set the adapter on the RV
        binding.rvMovies.setAdapter(movieAdapter);
        // Set a LM on the RV
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams p = new RequestParams();
        p.put("api_key", getString(R.string.MOVIE_DB_API_KEY));

        // Default to `original`
        final String[] size = {"original"};
        final String[] imageBaseUrl = {"https://image.tmdb.org/t/p/"};

        // Get poster size
        client.get(CONFIG_URL, p, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Config onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject baseObject = jsonObject.getJSONObject("images");
                    Log.i(TAG, "Config JSON Result: " + baseObject.toString());
                    imageBaseUrl[0] = baseObject.getString("secure_base_url");
                    JSONArray validSizes = baseObject.getJSONArray("poster_sizes");
                    // Extract middle size
                    size[0] = (String) validSizes.get(validSizes.length() / 2);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Config onFailure");
            }
        });

        client.get(NOW_PLAYING_URL, p, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "Now_Playing onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Now_Playing JSON Result: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results, size[0], imageBaseUrl[0]));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Now_Playing Hit JSON exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Now_Playing onFailure");

            }
        });
    }
}
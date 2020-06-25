package com.dtavana.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.dtavana.flixter.databinding.ActivityMovieDetailsBinding;
import com.dtavana.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MovieDetailsActivity";
    public static final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos";
    public static final String KEY_VIDEO_ID = "videoId";

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.rbVoteAverage.setRating((float) (movie.getVoteAverage() / 2));
        binding.tvRelease.setText(movie.getReleaseDate());
        binding.tvVoteCount.setText(movie.getVoteCount());

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams p = new RequestParams();
                p.put("api_key", getString(R.string.MOVIE_DB_API_KEY));

                if (movie.getVideoId() == null) {
                    Log.d(TAG, "videoId not cached, fetching from API");
                    client.get(String.format(VIDEO_URL, movie.getId()), p, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "Video onSuccess");
                            JSONObject jsonObject = json.jsonObject;
                            try {
                                JSONArray results = jsonObject.getJSONArray("results");
                                Log.i(TAG, "Video JSON Result: " + jsonObject.toString());
                                String videoId = ((JSONObject) results.get(0)).getString("key");
                                movie.setVideoId(videoId);
                                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                i.putExtra(KEY_VIDEO_ID, videoId);
                                startActivity(i);
                            } catch (JSONException e) {
                                Log.e(TAG, "Hit JSON exception", e);
                                Toast.makeText(MovieDetailsActivity.this, "Could not find a valid videoId", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "Video onFailure");
                            Toast.makeText(MovieDetailsActivity.this, "Could not find a valid videoId", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Log.d(TAG, "Using cached videoId value");
                    Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    i.putExtra(KEY_VIDEO_ID, movie.getVideoId());
                    startActivity(i);
                }
            }
        });
    }
}
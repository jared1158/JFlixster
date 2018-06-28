package com.jaredapps.jflixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jaredapps.jflixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    //base url for api
    public final static  String API_BASE_URL = "htpps://api.themoviedb.org/3";
    //the parameter name for the api key
    public final static String API_KEY_PARAM = "api_key";

    //tag foe logging form this activity
    public final static String TAG = "MovieListActivity";


    //instance fields
    AsyncHttpClient client;
    // the base urls for loading images
    String imageBaseUrl;

    //the poster size to use whe fetching images , part of the url
    String posterSize;

    //the list of currently playing movies
    ArrayList<Movie> movies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //intitialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies = new ArrayList<>();
        //get the configuration on app creation
        getConfiguration();

    }



    //get the list of currently playing movies
    private void getNowPlaying() {
        //create the url
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));//apikey
       //execute a GET request expecting a JSOn object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the reuslts into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create movie objects
                    for (int i =0; i < results.length(); i++){
                     Log.i(TAG, String.format("loaded %s movies" , results.length()));
                    }
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies" , e , true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get the data from now playing endpoint" , throwable , true);
            }
        });
}

    //get the configuration from the api
    private void getConfiguration(){
        //create the url
        String url = API_BASE_URL +"/configuration";
        //set request parameeters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// api key always required
        //execute aget request expecting a JSon object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject images = response.getJSONObject("images");
                    //get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    //get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3, "w342");
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s" , imageBaseUrl , posterSize));
                    //get the playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration" , throwable , true);
            }
        });



    }

//handle errors
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message , error);
        //alert user to avoid silent errors
        if (alertUser) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

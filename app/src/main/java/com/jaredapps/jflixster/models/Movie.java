package com.jaredapps.jflixster.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jared1158 on 6/27/18.
 */

public class Movie {

    //values from the api
    private String title;
    private String overview;
    private String posterPath; // only the path
    private String backdropPath;
    double voteAverage;

    // no arg, empty constructor required for parceler
    public Movie(){}

    //initialize from the JSon data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");

    }


    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
}

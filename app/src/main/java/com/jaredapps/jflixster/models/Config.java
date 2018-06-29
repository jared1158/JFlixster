package com.jaredapps.jflixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jared1158 on 6/28/18.
 */

public class Config {

    // the base urls for loading images
    String imageBaseUrl;

    //the poster size to use whe fetching images , part of the url
    String posterSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        //get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");

    }


    //helper method for creating urls

    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s" , imageBaseUrl, size, path); // concatenate
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }


}

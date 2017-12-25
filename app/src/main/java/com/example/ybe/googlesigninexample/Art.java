package com.example.ybe.googlesigninexample;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ybe on 12/15/17.
 */
@IgnoreExtraProperties

public class Art {
    private String artist, description, url;
    private int year;
    public int likeCount = 0;
    public Map<String, Boolean> likes = new HashMap<>();
    public int unlikeCount = 0;
    public Map<String, Boolean> unlikes = new HashMap<>();
    public Map<String, Boolean> library = new HashMap<>();

    public Art()    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Art(String artist, String description, String name, String url, int year) {
        this.artist = artist;
        this.description = description;
        this.url = url;
        this.year = year;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Art{" +
                "artist='" + artist + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", year=" + year +
                '}';
    }
    @Exclude
    public Map<String, Object> toMap()  {
        HashMap<String, Object> result = new HashMap<>();
        result.put("artist", artist);
        result.put("description", description);
        result.put("url", url);
        result.put("year", year);
        result.put("likeCount", likeCount);
        result.put("likes", likes);
        result.put("unlikeCount", unlikeCount);
        result.put("unlikes", unlikes);
        result.put("library", library);

        return result;
    }

}


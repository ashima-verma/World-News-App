package com.example.android.worldnews;

/**
 * Created by Om on 22-Apr-17.
 */

public class WorldNews {
    private String name;
    private String title;
    private String date;
    private String url;


    public WorldNews(String name1, String title1, String date1, String url1){
        name = name1;
        title = title1;
        date = date1;
        url = url1;
    }

    public String getName(){
        return name;
    }

    public String getTitle(){
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}

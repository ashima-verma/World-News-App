package com.example.android.worldnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Om on 22-Apr-17.
 */

public class WorldNewsLoader extends AsyncTaskLoader<List<WorldNews>> {

    private static final String LOG_TAG = WorldNewsLoader.class.getSimpleName();

    private String url;

    public WorldNewsLoader(Context context, String ur){
        super(context);
        url = ur;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }


    @Override
    public List<WorldNews> loadInBackground(){
        if (url == null){
            return null;
        }
        List<WorldNews> result = QueryUtils.fetchWorldNewsData(url);
        return result;
    }
}
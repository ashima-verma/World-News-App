package com.example.android.worldnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static com.example.android.worldnews.QueryUtils.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WorldNews>> {

    public static final String WORLD_NEWS_URL = "https://content.guardianapis.com/search?page-size=10&q=news&api-key=0fc305c0-c13b-40dd-b619-cc5149f58189";

    private WorldNewsAdapter newsadapter;
    private TextView emptyView;
    private static final int WORLD_NEWS_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.listview);

        emptyView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        newsadapter = new WorldNewsAdapter(this, new ArrayList<WorldNews>());

        newsListView.setAdapter(newsadapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
               WorldNews currentWorldNews = newsadapter.getItem(position);

                Uri worldNewsUri = Uri.parse(currentWorldNews.getUrl());
                Intent worldnewsIntent = new Intent(Intent.ACTION_VIEW, worldNewsUri);

                startActivity(worldnewsIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(WORLD_NEWS_LOADER_ID, null, this);
            Log.i(LOG_TAG, "Initialising the loader");
        }
        else {
            View spinner = findViewById(R.id.spinner);
            spinner.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<WorldNews>> onCreateLoader(int id, Bundle bundle ){
        Log.i(LOG_TAG, "Creating the Loader");
        return new WorldNewsLoader(this,WORLD_NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<WorldNews>> loader, List<WorldNews> data) {
        View spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        emptyView.setText(R.string.no_news);
        newsadapter.clear();
        if (data != null && !data.isEmpty()) {
            newsadapter.addAll(data);
        }
        Log.i(LOG_TAG, "Loader Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<WorldNews>> loader){
        newsadapter.clear();
        Log.i(LOG_TAG, "Loader reset");
    }
}
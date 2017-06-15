package com.example.android.worldnews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Om on 22-Apr-17.
 */

public class WorldNewsAdapter extends ArrayAdapter<WorldNews> {

    private static final String LOG_TAG = WorldNewsAdapter.class.getSimpleName();

    private static final String seprator = "T";

    public WorldNewsAdapter(Activity context, ArrayList<WorldNews> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        WorldNews currentWorldNews = getItem(position);

        TextView sectionName = (TextView) listItemView.findViewById(R.id.name);
        sectionName.setText(currentWorldNews.getName());

        TextView webTitle = (TextView) listItemView.findViewById(R.id.title);
        webTitle.setText(currentWorldNews.getTitle());

        String givenDate = currentWorldNews.getDate();
        String date1 = "";
        if (givenDate.contains(seprator)) {
            String[] parts = givenDate.split(seprator);
            date1 = parts[0];
        }
        TextView Date = (TextView) listItemView.findViewById(R.id.date);
        Date.setText(date1);
        return listItemView;
    }
}
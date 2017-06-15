package com.example.android.worldnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Om on 22-Apr-17.
 */

public class QueryUtils {
    private QueryUtils(){

    }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<WorldNews> fetchWorldNewsData (String reqUrl){
        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            Log.e(LOG_TAG, "Error in thread sleeping", e);
        }

        URL url2 = createUrl(reqUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url2);
        }

        catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream" , e);
        }

        List<WorldNews> worldNews =extractFeatureFromJson(jsonResponse);
        Log.i(LOG_TAG, "Fetching Earthquake Data");
        return worldNews;
    }

    private static URL createUrl(String StringUrl){
        URL url3 = null;
        try{
            url3 = new URL(StringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating url");
        }
        return url3;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            Log.i(LOG_TAG, "makeHttpRequest" + url);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<WorldNews> extractFeatureFromJson(String worldNewsJson){
        if (TextUtils.isEmpty(worldNewsJson)) {
            return null;
        }

        List<WorldNews> news = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(worldNewsJson);

            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray worldNewsArray = response.getJSONArray("results");

            for (int i = 0; i < worldNewsArray.length(); i++) {
                JSONObject currentNews = worldNewsArray.getJSONObject(i);
                String name = currentNews.optString("sectionName");

                String title = currentNews.optString("webTitle");

                String date = currentNews.optString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = currentNews.optString("webUrl");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                WorldNews worldNews = new WorldNews(name, title , date, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                news.add(worldNews);
            }
        }
        catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return news;
    }

}

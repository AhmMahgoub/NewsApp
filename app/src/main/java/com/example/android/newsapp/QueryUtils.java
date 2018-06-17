package com.example.android.newsapp;

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

import static com.example.android.newsapp.NewsActivity.LOG_TAG;

public final class QueryUtils {
    private QueryUtils() {
    }

    private static List<News> extractEarthquakes(String newsJSON) {

        List<News> news = new ArrayList<>();

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i <= resultArray.length(); i++) {
                JSONObject newsDetails = resultArray.getJSONObject(i);
                String section = newsDetails.getString("sectionId");
                String tilte = newsDetails.getString("webTitle");
                String date = newsDetails.getString("webPublicationDate");
                String url = newsDetails.getString("webUrl");

                JSONArray tagsArray = newsDetails.getJSONArray("tags");
                JSONObject tagsDetails = tagsArray.getJSONObject(0);
                String author = tagsDetails.getString("webTitle");

                News Jsonnews = new News(section, tilte, date, url, author);
                news.add(Jsonnews);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return news;
    }

    public static List<News> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        List<News> NewFromJson = extractEarthquakes(jsonResponse);

        return NewFromJson;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}


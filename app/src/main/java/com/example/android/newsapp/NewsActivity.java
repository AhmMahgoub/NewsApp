package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    //private static final String SAMPLE_JSON_RESPONSE = "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page-size=15&q=politics&api-key=75d943ec-c2c7-4e5b-a30f-da6a00936f37";
    private static final String SAMPLE_JSON_RESPONSE = "https://content.guardianapis.com/search";

    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingSpinner;
    private NetworkInfo activeNetwork;
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        final ListView newsListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        newsListView.setEmptyView(mLoadingSpinner);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mLoadingSpinner.setVisibility(View.GONE);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = mAdapter.getItem(i);
                Uri newsUri = Uri.parse(news.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webIntent);
            }
        });
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_min_PageSize_key),
                getString(R.string.settings_min_PageSize_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("q", "politics");
        uriBuilder.appendQueryParameter("api-key", "75d943ec-c2c7-4e5b-a30f-da6a00936f37");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        if(news == null){
            mEmptyStateTextView.setText(R.string.no_news);
            mLoadingSpinner.setVisibility(View.GONE);
        }
        else if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            mLoadingSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

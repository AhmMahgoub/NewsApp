package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String SAMPLE_JSON_RESPONSE = "http://content.guardianapis.com/search?q=debates&api-key=test";
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingSpinner;
    private NetworkInfo activeNetwork;
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        final ListView newsListView = (ListView)findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mLoadingSpinner =(ProgressBar) findViewById(R.id.loading_spinner);
        newsListView.setEmptyView(mLoadingSpinner);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        else{
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mLoadingSpinner.setVisibility(View.GONE);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = mAdapter.getItem(i);
                Uri earthquakeUri = Uri.parse(news.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(webIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            mLoadingSpinner.setVisibility(View.GONE);
            if(mAdapter.isEmpty()){
                mEmptyStateTextView.setText(R.string.no_news);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    private static class NewsLoader extends AsyncTaskLoader<List<News>> {
        /** Tag for log messages */
        private static final String LOG_TAG = NewsLoader.class.getName();
        private String mUrl;
        public NewsLoader(Context context, String url) {
            super(context);
            mUrl = url;
            // TODO: Finish implementing this constructor
        }

        @Override
        protected void onStartLoading() {

            forceLoad();
        }

        @Override
        public List<News> loadInBackground() {

            if (mUrl == null) {
                return null ;
            }
            // TODO: Implement this method
            List<News> news = QueryUtils.fetchEarthquakeData(mUrl);
            return news;
        }
    }
}

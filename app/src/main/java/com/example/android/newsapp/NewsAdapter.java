package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);
        TextView sectionText = (TextView) listItemView.findViewById(R.id.section);
        sectionText.setText(currentNews.getmSection());

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setText(currentNews.getmTitle());

        String dateAndTime = currentNews.getmDateAndTime();
        if(dateAndTime.contains("T")){
            String parts[] = dateAndTime.split("T");
            String date = parts[0];
            TextView dateText = (TextView)listItemView.findViewById(R.id.date);
            dateText.setText(date);
        }
        return listItemView;
    }
}

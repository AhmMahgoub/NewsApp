package com.example.android.newsapp;

public class News {

    private String mSection;
    private String mTitle;
    private String mDateAndTime;
    private String mUrl;

    News(String section , String title , String dateAndTime, String Url){
        mSection = section;
        mTitle = title;
        mDateAndTime = dateAndTime;
        mUrl = Url;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDateAndTime() {
        return mDateAndTime;
    }


    public String getmUrl() {
        return mUrl;
    }
}

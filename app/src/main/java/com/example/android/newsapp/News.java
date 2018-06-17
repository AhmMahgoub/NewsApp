package com.example.android.newsapp;

public class News {

    private String mSection;
    private String mTitle;
    private String mDateAndTime;
    private String mUrl;
    private String mAuthor = NO_AUTHOR_PROVIDED;
    private static final String NO_AUTHOR_PROVIDED = null;

    News(String section, String title, String dateAndTime, String Url, String author) {
        mSection = section;
        mTitle = title;
        mDateAndTime = dateAndTime;
        mUrl = Url;
        mAuthor = author;
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

    public String getmAuthor() {
        return mAuthor;
    }

    public boolean hasAuthor() {
        return mAuthor != NO_AUTHOR_PROVIDED;
    }
}

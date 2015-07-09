package com.yavor.popularmovies;

import java.util.Date;

public class MovieInfo {
    private boolean mAdult;
    private int[] mGenreIds;
    private int mId;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private String mOverview;
    private Date mReleaseDate;
    private String mPosterPath;
    private double mPopularity;
    private String mTitle;
    private boolean mVideo;
    private double mVoteAverage;
    private double mVoteCount;

    public boolean isAdult() {
        return mAdult;
    }

    public void setAdult(boolean adult) {
        this.mAdult = adult;
    }

    public int[] getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.mGenreIds = genreIds;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        this.mPopularity = popularity;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public void setVideo(boolean video) {
        this.mVideo = video;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public double getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(double voteCount) {
        this.mVoteCount = voteCount;
    }
}

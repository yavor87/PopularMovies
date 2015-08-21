package com.yavor.popularmovies.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.yavor.popularmovies.R;

public class YoutubePlayView extends View implements View.OnClickListener {

    public YoutubePlayView(Context context) {
        super(context);

        setOnClickListener(this);
    }

    public YoutubePlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.YoutubePlayView,
                0, 0);

        try {
            int fillColor = a.getColor(R.styleable.YoutubePlayView_fill, 0);
            mFill = new Paint();
            mFill.setColor(fillColor);
            mFill.setStyle(Paint.Style.FILL);
        } finally {
            a.recycle();
        }

        setOnClickListener(this);
    }

    private Paint mFill;
    private Path mPath = new Path();
    private Uri mVideoPath;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // triangle
        mPath.reset();
        mPath.lineTo(0, 0);
        mPath.lineTo(width, height / 2);
        mPath.lineTo(0, height);
        mPath.close();

        canvas.drawPath(mPath, mFill);
    }

    public Paint getFill() {
        return mFill;
    }

    public void setFill(Paint fill) {
        mFill = fill;
    }

    public Uri getVideoPath() {
        return mVideoPath;
    }

    public void setVideoPath(Uri videoPath) {
        mVideoPath = videoPath;
    }

    public static Uri createVideoPath(String site, String key) {
        if (site != null && key != null) {
            // Create YouTube link
            if (site.toLowerCase().equals("youtube")) {
                return Uri.parse("https://www.youtube.com/")
                        .buildUpon().appendPath("watch")
                        .appendQueryParameter("v", key)
                        .build();
            }
            // Handle other sites
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (mVideoPath == null)
            return;

        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(mVideoPath);
        getContext().startActivity(viewIntent);
    }
}

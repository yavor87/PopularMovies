package com.yavor.popularmovies.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yavor.popularmovies.R;

public class YoutubePlayView extends View {

    public YoutubePlayView(Context context) {
        super(context);
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
    }

    public YoutubePlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint mFill;
    private Path mPath = new Path();

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

    public void setFill(Paint mFill) {
        this.mFill = mFill;
    }
}

package com.android.samsung.codelab.guestbookdapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint mPaint;
    private float mPaddingStart;
    private float mPaddingEnd;

    /**
     * Create a decoration that draws a line in the given color and width between the items in the view.
     *
     * @param context  a context to access the resources.
     * @param color    the color of the separator to draw.
     * @param heightDp the height of the separator in dp.
     */
    public DividerItemDecoration(@NonNull Context context
            , @ColorInt int color
            , @FloatRange(from = 0, fromInclusive = false) float heightDp) {

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        final float thickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                heightDp, context.getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(thickness);
        mPaddingStart = 0;
        mPaddingEnd = 0;
    }

    public DividerItemDecoration(@NonNull Context context
            , @ColorInt int color
            , @FloatRange(from = 0, fromInclusive = false) float heightDp
            , int paddingStart
            , int paddingEnd) {

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setPathEffect(new DashPathEffect(new float[]{15, 5}, 0));

        final float thickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                heightDp, context.getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(thickness);

        mPaddingStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , paddingStart
                , context.getResources().getDisplayMetrics());
        mPaddingEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , paddingEnd
                , context.getResources().getDisplayMetrics());
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        // we want to retrieve the position in the list
        final int position = params.getViewAdapterPosition();

        // and add a separator to any view but the last one
        if (position < state.getItemCount()) {
            outRect.set(0, 0, 0, (int) mPaint.getStrokeWidth()); // left, top, right, bottom
        } else {
            outRect.setEmpty(); // 0, 0, 0, 0
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // we set the stroke width before, so as to correctly draw the line we have to offset by width / 2
        final int offset = (int) (mPaint.getStrokeWidth() / 2);

        // this will iterate over every visible view
        for (int i = 0; i < parent.getChildCount(); i++) {
            // get the view
            final View view = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            // get the position
            final int position = params.getViewAdapterPosition();

            // and finally draw the separator
            if (position < state.getItemCount() - 1) {
                c.drawLine(view.getLeft() + mPaddingStart, view.getBottom() + offset, view.getRight() - mPaddingEnd, view.getBottom() + offset, mPaint);
            }
        }
    }
}


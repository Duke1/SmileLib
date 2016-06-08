package xyz.openhh.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import xyz.openhh.R;


/**
 * @author HH
 */
public class ItemSpanDecoration extends RecyclerView.ItemDecoration {

    Paint mPaint = new Paint();
    float mSpanLineHeight = 0f;
    float mBottomPadding = 0.0f;

    float mLeftPadding = 0.0f;

    public ItemSpanDecoration(Context ctx) {
        this(ctx, ctx.getResources().getColor(R.color.light_gray));//Color.parseColor("#D9D9D9"));
    }

    public ItemSpanDecoration(Context ctx, int color) {
        mPaint.setColor(color);//Color.parseColor("#D9D9D9"));
        mPaint.setAntiAlias(true);


        mSpanLineHeight = ctx.getResources().getDimensionPixelSize(R.dimen.app_divider_height);
        mBottomPadding = 0;
    }

    public void setLeftPadding(float leftPadding) {
        this.mLeftPadding = leftPadding;
    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawVertical(canvas, parent);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        //parent.getChildAdapterPosition(view);

        outRect.set(0, 0, 0, 0);//每个item底部偏移量
    }

    public void drawVertical(Canvas canvas, RecyclerView parent) {
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        final int lastIndex = childCount - 1;
        for (int i = 0; i < childCount; i++) {
//            if (i != lastIndex) continue;
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            float top = child.getBottom() + params.bottomMargin + mBottomPadding;
            float bottom = top + mSpanLineHeight;

            canvas.drawLine(mLeftPadding, top, right, bottom, mPaint);
        }
    }

}
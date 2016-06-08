/*
 * Copyright 2015 HH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.openhh.compat.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView extend.
 * <p>
 * Created by HH .
 */
public class AdmireListView extends RecyclerView {

    private LinearLayoutManager mLayoutManager;

    public AdmireListView(Context context) {
        super(context);
        init();
    }

    public AdmireListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdmireListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    protected void init() {

        setHasFixedSize(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(mLayoutManager);
        setItemAnimator(new DefaultItemAnimator());
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return mLayoutManager;
    }


    /**
     * Full
     */
    public static class FullyLinearLayoutManager extends LinearLayoutManager {


        public FullyLinearLayoutManager(Context context) {
            super(context);
        }


        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {

            final int widthMode = MeasureSpec.getMode(widthSpec);
            final int heightMode = MeasureSpec.getMode(heightSpec);
            final int widthSize = MeasureSpec.getSize(widthSpec);
            final int heightSize = MeasureSpec.getSize(heightSpec);


            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i,
                        MeasureSpec.makeMeasureSpec(i, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(i, MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case MeasureSpec.EXACTLY:
                    width = widthSize;
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case MeasureSpec.EXACTLY:
                    height = heightSize;
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
            try {
                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                            getPaddingLeft() + getPaddingRight(), p.width);

                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), p.height);

                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }


    public void scrollToPositionWithOffset(int position, int offset) {
        mLayoutManager.scrollToPositionWithOffset(position, offset);
    }
}

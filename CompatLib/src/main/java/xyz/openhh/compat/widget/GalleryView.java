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
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


import java.util.ArrayList;

import xyz.openhh.compat.R;


/**
 * @author HH
 */
public class GalleryView extends RecyclerView {

    public final static int LEVEL_PRIMARY = 1;
    public final static int LEVEL_SECONDARY = 2;


    DisplayMetrics mDisplayMetrics;
    int mSpace;
    ArrayList<CardGalleryEvent> mCardGalleryEventList;

    public GalleryView(Context context) {
        this(context, null);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        final int cardWidth;
        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CompatSwipeRefreshLayout);
            cardWidth = typedArray.getDimensionPixelSize(R.styleable.GalleryView_card_width, 0);

            typedArray.recycle();
        } else cardWidth = 0;


        mCardGalleryEventList = new ArrayList<>();

        setHasFixedSize(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        mDisplayMetrics = getResources().getDisplayMetrics();
        this.addItemDecoration(new ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                int padding = (parent.getWidth() - cardWidth) / 2;

                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.left = padding;
                else if (parent.getChildAdapterPosition(view) == (parent.getAdapter().getItemCount() - 1)) {
                    outRect.left = mSpace;
                    outRect.right = padding;
                } else {
                    outRect.left = mSpace;
                }

            }
        });


        GalleryLayoutManager layoutManager = new GalleryLayoutManager(getContext());
        layoutManager.setOrientation(GalleryLayoutManager.HORIZONTAL);
        this.setLayoutManager(layoutManager);

        addOnScrollListener(new GalleryScrollListener());
    }

    public void setSpace(int space) {
        this.mSpace = space;
    }

    public class GalleryLayoutManager extends LinearLayoutManager {

        public GalleryLayoutManager(Context context) {
            super(context);
        }

        public GalleryLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public GalleryLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }


    public class GalleryScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            Point pCenterPoint = new Point(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (null == view) continue;
                if (view instanceof CardGalleryEvent) {
                    CardGalleryEvent eventView = (CardGalleryEvent) view;
                    Rect cRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (cRect.contains(pCenterPoint.x, pCenterPoint.y)) {
                        eventView.onCardLevelChanged(LEVEL_PRIMARY, view.getId());
                        for (CardGalleryEvent event : mCardGalleryEventList)
                            if (null != event)
                                event.onCardLevelChanged(LEVEL_PRIMARY, view.getId());
                    } else {
                        eventView.onCardLevelChanged(LEVEL_SECONDARY, view.getId());
                        for (CardGalleryEvent event : mCardGalleryEventList)
                            if (null != event)
                                event.onCardLevelChanged(LEVEL_SECONDARY, view.getId());
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (SCROLL_STATE_IDLE == newState) {

                Point pCenterPoint = new Point(getLeft() + getWidth() / 2, getTop() + getHeight() / 2);

                boolean hasCenterView = false;

                int childCount = getChildCount();
                View primaryView = null;
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    if (null == view) continue;
                    Rect cRect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (cRect.contains(pCenterPoint.x, pCenterPoint.y)) {
                        int lx = pCenterPoint.x - view.getWidth() / 2;
                        int dx = view.getLeft() - lx;
                        recyclerView.scrollBy(dx, 0);
                        hasCenterView = true;
                        primaryView = view;
                    }
                }

                //not found center view
                if (!hasCenterView) {
                    float minDistance = Float.MAX_VALUE;
                    View minDistanceView = null;
                    for (int i = 0; i < childCount; i++) {
                        View view = getChildAt(i);
                        if (null == view) continue;

                        int vx = view.getLeft() + view.getWidth() / 2;
                        if (vx <= minDistance) {
                            minDistance = vx;
                            minDistanceView = view;
                        }
                    }

                    int lx = pCenterPoint.x - minDistanceView.getWidth() / 2;
                    int dx = minDistanceView.getLeft() - lx;
                    recyclerView.scrollBy(dx, 0);
                    primaryView = minDistanceView;
                }

                for (CardGalleryEvent event : mCardGalleryEventList)
                    if (null != event) event.onScrollStopped(primaryView);
            }
        }
    }


    public void addCardGalleryEvent(CardGalleryEvent event) {
        if (null == mCardGalleryEventList)
            mCardGalleryEventList = new ArrayList<>();

        mCardGalleryEventList.add(event);
    }

    public void removeCardGalleryEvent(CardGalleryEvent event) {
        if (null != mCardGalleryEventList)
            mCardGalleryEventList.remove(event);
    }

    public interface CardGalleryEvent {
        /**
         * {@link #LEVEL_PRIMARY},{@link #LEVEL_SECONDARY}
         *
         * @param level
         */
        void onCardLevelChanged(int level, int position);

        void onScrollStopped(View primaryView);
    }

}

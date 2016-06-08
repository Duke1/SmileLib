/*
 * Copyright (C) 2013 The Android Open Source Project
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
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import xyz.openhh.compat.R;
import xyz.openhh.compat.util.ConvertHelper;

/**
 * Created by HH .
 * see： {@link android.support.v4.widget.SwipeRefreshLayout}
 */
public class CompatSwipeRefreshLayout extends ViewGroup implements NestedScrollingChild, NestedScrollingParent {


    public final static int MODE_NORMAL = 1;
    public final static int MODE_PULL_DOWN = MODE_NORMAL << 1;
    public final static int MODE_PULL_UP = MODE_NORMAL << 2;

    private static final String LOG_TAG = CompatSwipeRefreshLayout.class.getSimpleName();

    public final static int PULL_TO_STOP = 0;
    public final static int PULL_TO_REFRESH = 1;
    public final static int RELEASE_TO_REFRESH = 2;
    public final static int REFRESHING = 3;
    public final static int REFRESH_DONE = 4;


    private int mTouchSlop = 0;
    private View mTargetView;
    private float mStartY;
    private RelativeLayout mRefreshLayout;
    private View mHeadView = null;
    private int mRefreshLayoutHeight = 0;
    private int mCurPullState = PULL_TO_STOP;
    private int mPullLayoutHeight, mPullLayoutLoadingHeight;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    private static final float DRAG_RATE = .5f;

    private int mMode = MODE_NORMAL;

    private Adapter mAdapter;
    private Listener mListener;

    private float mTotalUnconsumed;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private boolean mNestedScrollInProgress;


    private boolean mIsBeingDragged = false;

    public CompatSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public CompatSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);


        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mPullLayoutHeight = ConvertHelper.dpToPx(context, 300);
        mPullLayoutLoadingHeight = ConvertHelper.dpToPx(context, 70);

        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CompatSwipeRefreshLayout);
            mMode = typedArray.getInt(R.styleable.CompatSwipeRefreshLayout_compat_mode, MODE_NORMAL);

            typedArray.recycle();
        }


        mRefreshLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.compat_swipe_refresh_layout, null, false);
        mRefreshLayout.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        addView(mRefreshLayout, lp);


        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        setNestedScrollingEnabled(true);
    }

    /**
     * @param mode MODE_NORMAL|MODE_PULL_DOWN|MODE_PULL_UP
     */
    public void setMode(int mode) {
        this.mMode = mode;
    }

    /**
     * is exist target mode.
     *
     * @param targetMode
     *
     * @return
     */
    public final boolean hasMode(int targetMode) {
        return (mMode & targetMode) == targetMode;
    }


    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return getCompatRawY(ev, index);
    }

    private void ensureTarget() {
        if (null == mHeadView || 0 == mRefreshLayout.getChildCount()) {
            if (null != mAdapter)
                mHeadView = mAdapter.onCreateHeadView(mRefreshLayout);
            if (null != mHeadView)
                mRefreshLayout.addView(mHeadView);
        }

        if (mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshLayout) {
                    mTargetView = child;
                    break;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTargetView == null) {
            ensureTarget();
        }
        if (mTargetView == null) {
            return;
        }

        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();

        int refreshLayoutWidth = mRefreshLayout.getMeasuredWidth();
        int refreshLayoutHeight = mRefreshLayoutHeight;
        mRefreshLayout.layout(childLeft, childTop, childLeft + refreshLayoutWidth, childTop + refreshLayoutHeight);

        childTop += refreshLayoutHeight;
        int childWidth = width - getPaddingLeft() - getPaddingRight();
        int childHeight = height - childTop - getPaddingBottom();
        mTargetView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTargetView == null) {
            ensureTarget();
        }
        if (mTargetView == null) {
            return;
        }

        mRefreshLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mRefreshLayoutHeight, MeasureSpec.UNSPECIFIED));

        mTargetView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mRefreshLayoutHeight, MeasureSpec.UNSPECIFIED));


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        final int action = MotionEventCompat.getActionMasked(event);

        if (canChildScrollUp() || !hasMode(MODE_PULL_DOWN) || REFRESHING == mCurPullState) {
            return false;
        }


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(event, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mStartY = initialDownY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_MOVE:

                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(event, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float deltaY = y - mStartY;
                if (deltaY > mTouchSlop && !mIsBeingDragged) {
                    mStartY = mStartY + mTouchSlop;
                    mIsBeingDragged = true;
                }
                setCurPullState(PULL_TO_REFRESH);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }


        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (canChildScrollUp() || !hasMode(MODE_PULL_DOWN) || REFRESHING == mCurPullState) {
            return false;
        }
        int pointerIndex = -1;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }

                if (RELEASE_TO_REFRESH == mCurPullState) {
                    setCurPullState(REFRESHING);
                    setRefreshLayoutHeight(mPullLayoutLoadingHeight);
                    if (null != mListener) mListener.onRefresh(this);
                } else {
                    closeRefreshBoard();
                }

                mStartY = 0;

                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                return false;
            }
            case MotionEvent.ACTION_MOVE:
                pointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                setCurPullState(PULL_TO_REFRESH);
                float deltaY = getCompatRawY(event, pointerIndex) - mStartY;
                deltaY = deltaY * DRAG_RATE;
                if (mIsBeingDragged) {
                    if (deltaY > 0) {
                        if (mRefreshLayout.getHeight() > (mPullLayoutHeight / 2))
                            setCurPullState(RELEASE_TO_REFRESH);

                        setRefreshLayoutHeight((int) deltaY);
                    } else
                        return false;

                }
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
                pointerIndex = MotionEventCompat.getActionIndex(event);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                mActivePointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }


        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void setCurPullState(int state) {
        mCurPullState = state;


        CsView csView = (CsView) mHeadView;

        csView.onStatusChanged(state);

    }

    public void closeRefreshBoard() {
        setCurPullState(REFRESH_DONE);

        setRefreshLayoutHeight(0);
    }


    private boolean canChildScrollUp() {

        return ViewCompat.canScrollVertically(mTargetView, -1);

    }

    private int getCompatRawY(MotionEvent event, int pointerIndex) {
        return (int) event.getY(pointerIndex);
    }


    private void setRefreshLayoutHeight(float height) {
        ViewGroup.LayoutParams layoutParams = mRefreshLayout.getLayoutParams();
        layoutParams.height = (int) Math.abs(height);

        if (layoutParams.height > mPullLayoutHeight)
            layoutParams.height = mPullLayoutHeight;

        mRefreshLayout.setLayoutParams(layoutParams);


        mRefreshLayoutHeight = layoutParams.height;

        int targetHeight = getHeight() - mRefreshLayoutHeight;
        ViewGroup.LayoutParams targetLayoutParams = mTargetView.getLayoutParams();
        targetLayoutParams.height = targetHeight;
        mTargetView.setLayoutParams(targetLayoutParams);

        requestLayout();
        invalidate();
    }


    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public static abstract class Adapter {

        public abstract View onCreateHeadView(RelativeLayout parent);

        public abstract View onCreateFootView(RelativeLayout parent);
    }


    public interface CsView {
        /**
         * pull status changed.
         *
         * @param status {@link CompatSwipeRefreshLayout#PULL_TO_REFRESH}
         */
        void onStatusChanged(int status);
    }


    public interface Listener {
        void onRefresh(CompatSwipeRefreshLayout layout);
    }


    ////////////Nested
    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && REFRESHING != mCurPullState
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            setRefreshLayoutHeight(mTotalUnconsumed);
        }


        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            setRefreshLayoutHeight(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            setRefreshLayoutHeight(mTotalUnconsumed);
        }
    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {

        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }


    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


}

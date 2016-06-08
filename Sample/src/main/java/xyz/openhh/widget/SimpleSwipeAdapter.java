package xyz.openhh.widget;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import xyz.openhh.compat.widget.CompatSwipeRefreshLayout;

/**
 * Created by HH .
 */
public class SimpleSwipeAdapter extends CompatSwipeRefreshLayout.Adapter {
    @Override
    public View onCreateHeadView(RelativeLayout parent) {
        SimpleHeadRefreshView headRefreshView = new SimpleHeadRefreshView(parent.getContext());
        RelativeLayout.LayoutParams animLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        animLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        animLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        headRefreshView.setLayoutParams(animLp);
        headRefreshView.setGravity(Gravity.CENTER);
        headRefreshView.setTextColor(Color.RED);
        return headRefreshView;
    }

    @Override
    public View onCreateFootView(RelativeLayout parent) {
        return null;
    }
}

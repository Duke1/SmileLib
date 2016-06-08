package xyz.openhh.widget;

import android.content.Context;
import android.widget.TextView;


import xyz.openhh.compat.widget.CompatSwipeRefreshLayout;

/**
 * Created by HH .
 */
public class SimpleHeadRefreshView extends TextView implements CompatSwipeRefreshLayout.CsView {
    public SimpleHeadRefreshView(Context context) {
        super(context);
    }



    @Override
    public void onStatusChanged(int status) {
        switch (status) {
            case CompatSwipeRefreshLayout.PULL_TO_STOP:
                setText("停止");
                break;
            case CompatSwipeRefreshLayout.PULL_TO_REFRESH:
                setText("下拉刷新");
                break;
            case CompatSwipeRefreshLayout.RELEASE_TO_REFRESH:
                setText("松开刷新###");
                break;
            case CompatSwipeRefreshLayout.REFRESHING:
                setText("Loading.....");

                break;
            case CompatSwipeRefreshLayout.REFRESH_DONE:
                setText("Done");
                break;
        }
    }
}

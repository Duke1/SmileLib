package xyz.openhh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.openhh.R;
import xyz.openhh.compat.widget.CompatSwipeRefreshLayout;
import xyz.openhh.widget.SimpleSwipeAdapter;

/**
 * Created by Duke .
 */
public class HelpActivity extends BaseActivity {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, HelpActivity.class);
        activity.startActivity(intent);
    }


    @Bind(R.id.compat_swipe_refresh_layout)
    CompatSwipeRefreshLayout mCompatSwipeRefreshLayout;
    @Bind(R.id.head_tab_layout)
    TabLayout headTabLayout;


    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        mCompatSwipeRefreshLayout.setAdapter(new SimpleSwipeAdapter());
        mCompatSwipeRefreshLayout.setListener(new CompatSwipeRefreshLayout.Listener() {
            @Override
            public void onRefresh(final CompatSwipeRefreshLayout layout) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.closeRefreshBoard();
                    }
                }, 5000);
            }
        });


        headTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        headTabLayout.addTab(headTabLayout.newTab().setText("Tab1"));

        headTabLayout.addTab(headTabLayout.newTab().setText("Tab2"));

        headTabLayout.addTab(headTabLayout.newTab().setText("Tab3"));

        headTabLayout.addTab(headTabLayout.newTab().setText("Tab4"));
        headTabLayout.addTab(headTabLayout.newTab().setText("Tab4"));
    }
}

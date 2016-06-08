package xyz.openhh.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.openhh.R;
import xyz.openhh.activity.MainActivity;
import xyz.openhh.adapter.ArchiveListAdapter;
import xyz.openhh.bean.ArchiveInfo;
import xyz.openhh.bean.Department;
import xyz.openhh.bean.ListItem;
import xyz.openhh.biz.DepartmentBiz;
import xyz.openhh.compat.widget.AdmireListView;
import xyz.openhh.compat.widget.CompatSwipeRefreshLayout;
import xyz.openhh.util.SimpleSubscriber;
import xyz.openhh.widget.ItemSpanDecoration;
import xyz.openhh.widget.SimpleSwipeAdapter;

/**
 * Created by HH .
 */
public class HelperDocFragment extends BaseFragment {

    @Bind(R.id.compat_swipe_refresh_layout)
    CompatSwipeRefreshLayout mCompatSwipeRefreshLayout;


    MainActivity mMainActivity;

    Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help_doc, container, false);
        ButterKnife.bind(this, rootView);

        initRootView(rootView);

        init();
        return rootView;
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


    }

}

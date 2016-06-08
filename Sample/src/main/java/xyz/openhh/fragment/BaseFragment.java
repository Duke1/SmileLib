package xyz.openhh.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import xyz.openhh.activity.MainActivity;

/**
 * @author HH
 */
public abstract class BaseFragment extends Fragment {


    public DisplayMetrics mDisplayMetrics;

    private boolean destroyed;


    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mActivity = getActivity();
    }

    public void setTitle(CharSequence title) {
        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        if (null != compatActivity) {
            ActionBar actionBar = compatActivity.getSupportActionBar();
            if (null != actionBar) {
                actionBar.setTitle(title);
            }
        }
    }

    public void setTitle(int resId) {
        Activity activity = getActivity();
        if (null != activity) {
            setTitle(activity.getString(resId));
        }
    }


    @Override
    public void onDetach() {
        if (17 > Build.VERSION.SDK_INT) {
            destroyed = true;
        }
        super.onDetach();
    }

    public boolean isDestroyed() {
        if (17 > Build.VERSION.SDK_INT) {
            return destroyed;
        } else {
            return super.isDetached();
        }
    }


    protected void initRootView(View rootView) {
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}

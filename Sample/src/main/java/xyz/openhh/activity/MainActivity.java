package xyz.openhh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.openhh.R;
import xyz.openhh.fragment.BaseFragment;
import xyz.openhh.util.DataHelper;

/**
 * @author HH
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_USER_INFO = "_user_info";

    @Bind(R.id.drawer)
    DrawerLayout drawer;

    @Bind(R.id.fundation_name)
    public TextView mFundationName;


    public DataHelper dataHelper;
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStyle();
        drawer.setDrawerListener(this);

        dataHelper = OpenHelperManager.getHelper(this, DataHelper.class);
        dataHelper.getWritableDatabase();
    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable(EXTRA_USER_INFO, WisapeApplication.getInstance().getUserInfo());
//    }

    private void initStyle() {
        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /*
     * DrawerLayout.DrawerListener
     */
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        View contentView = drawer.findViewById(R.id.main_view);
        float scale = 1 - slideOffset;
        float rightScale = 0.8f + scale * 0.2f;
        DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) drawerView.getLayoutParams();
        if (Gravity.LEFT == lp.gravity) {
            float leftScale = 1 - 0.3f * scale;
            drawerView.setScaleX(leftScale);
            drawerView.setScaleY(leftScale);
            drawerView.setAlpha(0.6f + 0.4f * (1 - scale));

            contentView.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
            contentView.setPivotX(0);
            contentView.setPivotY(contentView.getMeasuredHeight() / 2);
            contentView.invalidate();
            contentView.setScaleX(rightScale);
            contentView.setScaleY(rightScale);
        } else {
            contentView.setTranslationX(-drawerView.getMeasuredWidth() * slideOffset);
            contentView.setPivotX(contentView.getMeasuredWidth());
            contentView.setPivotY(contentView.getMeasuredHeight() / 2);
            contentView.invalidate();
            contentView.setScaleX(rightScale);
            contentView.setScaleY(rightScale);
        }
    }

    /*
    * DrawerLayout.DrawerListener
    */
    @Override
    public void onDrawerOpened(View drawerView) {
    }

    /*
    * DrawerLayout.DrawerListener
    */
    @Override
    public void onDrawerClosed(View drawerView) {
    }

    /*
    * DrawerLayout.DrawerListener
    */
    @Override
    public void onDrawerStateChanged(int newState) {

    }

    public final void openOrCloseMainMenu() {
        if (null != drawer) {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else {
                drawer.openDrawer(Gravity.LEFT);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            openOrCloseMainMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenHelperManager.releaseHelper();
        ButterKnife.unbind(this);
        if (null != drawer) {
            drawer.setOnDragListener(null);
            drawer = null;
        }
    }

    @OnClick(R.id.menu_switch)
    public void onMenuSwitchClick(View view) {
        openOrCloseMainMenu();
    }


    public void setFundationName(String name) {
        mFundationName.setText(name);
    }


    public void openFundationFragment(BaseFragment baseFragment, Bundle bundle) {
        Intent intent = new Intent();
        if (null != bundle) intent.putExtras(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_function_layout, baseFragment);
        transaction.commit();
    }
}

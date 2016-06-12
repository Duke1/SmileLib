package xyz.openhh.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import xyz.openhh.R;


/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setSupportActionBar();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
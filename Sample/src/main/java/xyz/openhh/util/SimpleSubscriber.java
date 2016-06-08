package xyz.openhh.util;

import android.content.Context;
import android.widget.Toast;

import rx.Subscriber;

/**
 * 订阅者
 * Created by HH .
 */
public class SimpleSubscriber<T> extends Subscriber<T> {
    private Context context;

    public SimpleSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        Toast.makeText(context, "异常", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T o) {

    }
}

package xyz.openhh.compat.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by HH .
 */
public class ConvertHelper {

    public final static int dpToPx(Context ctx, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, ctx.getResources().getDisplayMetrics());
    }
}

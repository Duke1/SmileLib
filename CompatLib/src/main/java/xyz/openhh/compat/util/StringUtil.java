package xyz.openhh.compat.util;

/**
 * Created by HH .
 */
public class StringUtil {
    /**
     *
     * @param str
     * @return
     */
    public final static boolean isNull(String str) {
        return null == str || "".equals(str.trim()) ? true : false;
    }
}

package xyz.openhh.bean;

/**
 * Created by HH .
 */
public class ListItem<T> {
    public int type;
    public T data;

    public ListItem(int type, T data) {
        this.type = type;
        this.data = data;
    }
}

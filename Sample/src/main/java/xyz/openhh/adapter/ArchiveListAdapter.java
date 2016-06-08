package xyz.openhh.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static xyz.openhh.compat.util.StringUtil.isNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.openhh.R;
import xyz.openhh.bean.ArchiveInfo;
import xyz.openhh.bean.ListItem;

/**
 * Created by HH .
 */
public class ArchiveListAdapter extends RecyclerView.Adapter {
    public static final int TYPE_UNKNOW = 0,
            TYPE_GROUP = TYPE_UNKNOW + 1,
            TYPE_PEOPLE = TYPE_GROUP + 1;

    protected Activity mActivity;
    private OnItemOptListener<ArchiveInfo> mOnItemOptListener;

    private List<ListItem> mListItem = new ArrayList<>();


    public ArchiveListAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void setListItem(List<ListItem> list) {
        this.mListItem.clear();
        this.mListItem.addAll(0, list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ArchiveListAdapter.TYPE_GROUP:
                viewHolder = GroupViewHolder.create(mActivity);
                break;
            case ArchiveListAdapter.TYPE_PEOPLE:
                viewHolder = ArchiveViewHolder.create(mActivity);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        if (ArchiveListAdapter.TYPE_GROUP == type) {

            ListItem<String> groupItem = mListItem.get(position);
            ((GroupViewHolder) holder).groupName.setText(groupItem.data);
        } else if (ArchiveListAdapter.TYPE_PEOPLE == type) {

            ListItem<ArchiveInfo> peopleHubListItem = mListItem.get(position);
            ((ArchiveViewHolder) holder).name.setText(peopleHubListItem.data.name);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return mListItem.get(position).type;
    }

    @Override
    public int getItemCount() {
        if (null == mListItem || mListItem.size() == 0) return 0;
        return mListItem.size();
    }

    /**
     * 获取指定组的开始position
     *
     * @return
     */
    public int getGroupPosition(String keyStr) {
        if (isNull(keyStr)) return -1;
        for (int i = 0, len = (null == mListItem ? 0 : mListItem.size()); i < len; i++) {
            ListItem listItem = mListItem.get(i);
            if (TYPE_GROUP == listItem.type) {
                if (keyStr.equalsIgnoreCase((String) listItem.data)) {
                    return i;
                }
            }
        }

        return -1;
    }


    public void setOnItemOptListener(OnItemOptListener<ArchiveInfo> listener) {
        this.mOnItemOptListener = listener;
    }


    static class ArchiveViewHolder extends RecyclerView.ViewHolder {
        public static ArchiveViewHolder create(Activity activity) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_archive_list_item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.getResources().getDimensionPixelSize(R.dimen.standard_height)));
            return new ArchiveViewHolder(view, activity);
        }

        private Activity activity;
        @Bind(R.id.name)
        public android.support.v7.widget.AppCompatCheckedTextView name;


        public ArchiveViewHolder(View itemView, Activity activity) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }


        @OnClick(R.id.name)
        public void onCbNameClick(View view) {
        }


    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.group_name)
        public TextView groupName;

        public static GroupViewHolder create(Context ctx) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_list_group_item, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ctx.getResources().getDimensionPixelSize(R.dimen.standard_height)));

            return new GroupViewHolder(view);
        }

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    /**
     * 列表操作Listener
     */
    public interface OnItemOptListener<OI> {
        void onItemClick(RecyclerView.ViewHolder holder, OI contactPeopleInfo);
    }
}

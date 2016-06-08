package xyz.openhh.fragment;

import android.os.Bundle;
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
import xyz.openhh.util.SimpleSubscriber;
import xyz.openhh.widget.ItemSpanDecoration;

/**
 * Created by HH .
 */
public class ContractManageFragment extends BaseFragment {
    @Bind(R.id.menu_btn)
    AddFloatingActionButton mMenuBtn;
    @Bind(R.id.list)
    AdmireListView mArchiveList;

    ArchiveListAdapter mArchiveListAdapter;

    MainActivity mMainActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contract_manage, container, false);
        ButterKnife.bind(this, rootView);

        initRootView(rootView);

        init();
        return rootView;
    }

    private void init() {
        ItemSpanDecoration itemSpanDecoration = new ItemSpanDecoration(mActivity);
        itemSpanDecoration.setLeftPadding(getResources().getDimension(R.dimen.span_space));
        mArchiveList.addItemDecoration(itemSpanDecoration);

        mMenuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mMainActivity.openFundationFragment(new ModifyArchiveFragment(), null);
            }
        });


        mArchiveListAdapter = new ArchiveListAdapter(mActivity);

        mArchiveList.setAdapter(mArchiveListAdapter);

        loadData();
    }


    private void loadData() {
        Observable.just("")
                .map(new Func1<String, List<ArchiveInfo>>() {
                    @Override
                    public List<ArchiveInfo> call(String string) {
                        List<ArchiveInfo> list = new ArrayList<ArchiveInfo>();

                        for (int i = 0; i < 10; i++) {
                            ArchiveInfo archiveInfo = new ArchiveInfo();
                            archiveInfo.archiveId = i;
                            archiveInfo.birthday = new Date();
                            archiveInfo.degree = "本科";
                            archiveInfo.departmentId = (0 == i % 2 ? 1 : (1 == i % 2 ? 3 : 2));
                            archiveInfo.name = "名字" + i + "的合同";

                            list.add(archiveInfo);
                        }
                        return list;
                    }
                })
                .map(new Func1<List<ArchiveInfo>, List<ListItem>>() {
                    @Override
                    public List<ListItem> call(List<ArchiveInfo> archiveInfoList) {

                        List<Department> departments = DepartmentBiz.getInstance().getDepartmentList(((MainActivity) mActivity).dataHelper);

                        List<ListItem> itemList = new ArrayList<>();

                        for (int i = 0; i < departments.size(); i++) {
                            Department department = departments.get(i);
                            itemList.add(new ListItem(ArchiveListAdapter.TYPE_GROUP, department.name));
                            for (int j = 0, len = archiveInfoList.size(); j < len; j++) {
                                ArchiveInfo archiveInfo = archiveInfoList.get(j);
                                if (archiveInfo.departmentId == department.departmentId) {
                                    itemList.add(new ListItem<ArchiveInfo>(ArchiveListAdapter.TYPE_PEOPLE, archiveInfo));


                                    archiveInfoList.remove(j);

                                    //因为移除之后数量变化，不处理会有IndexOutOfBoundsException错误
                                    --len;
                                    --j;
                                }
                            }
                        }

                        return itemList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<ListItem>>(mActivity) {
                    @Override
                    public void onNext(List<ListItem> listItems) {

                        mArchiveListAdapter.setListItem(listItems);
                    }


                });
    }
}

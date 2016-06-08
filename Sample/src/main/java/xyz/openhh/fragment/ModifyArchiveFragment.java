package xyz.openhh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.getbase.floatingactionbutton.AddFloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.openhh.R;
import xyz.openhh.util.SimpleSubscriber;

/**
 * Created by HH on 2016/4/18.
 */
public class ModifyArchiveFragment extends BaseFragment {


    @Bind(R.id.menu_btn)
    AddFloatingActionButton mMenuBtn;
    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.sex)
    EditText mSex;
    @Bind(R.id.nation)
    EditText mNation;
    @Bind(R.id.birthday)
    EditText mBirthday;
    @Bind(R.id.nativePlace)
    EditText mNativePlace;
    @Bind(R.id.familyType)
    EditText mFamilyType;
    @Bind(R.id.marital_status)
    EditText mMaritalStatus;
    @Bind(R.id.degree)
    EditText mDegree;
    @Bind(R.id.political_level)
    EditText mPoliticalLevel;
    @Bind(R.id.health_status)
    EditText mHealthStatus;
    @Bind(R.id.id_card)
    EditText mIdCard;
    @Bind(R.id.house_address)
    EditText mHouseAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_modify_archive, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }


    private void saveData() {
        Observable.just("")
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String string) {
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Boolean>(mActivity) {
                    @Override
                    public void onNext(Boolean o) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

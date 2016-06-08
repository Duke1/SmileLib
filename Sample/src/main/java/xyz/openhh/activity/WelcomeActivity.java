package xyz.openhh.activity;

import android.os.Bundle;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import xyz.openhh.R;
import xyz.openhh.bean.Department;
import xyz.openhh.biz.DepartmentBiz;
import xyz.openhh.util.DataHelper;

/**
 * Created by HH .
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome);

        initData();
    }


    private void initData() {
        DataHelper dataHelper = OpenHelperManager.getHelper(this, DataHelper.class);
        dataHelper.getWritableDatabase();
        List list = DepartmentBiz.getInstance().getDepartmentList(dataHelper);
        if (null == list || 0 == list.size()) {//默认部门

            DepartmentBiz.getInstance().addDepartment(dataHelper, new Department("行政人事部", "HR", 0));
            DepartmentBiz.getInstance().addDepartment(dataHelper, new Department("研发部", "R&D", 0));
            DepartmentBiz.getInstance().addDepartment(dataHelper, new Department("设计部", "Design Department", 0));
        }

        OpenHelperManager.releaseHelper();

        MainActivity.launch(this);
        finish();
    }
}

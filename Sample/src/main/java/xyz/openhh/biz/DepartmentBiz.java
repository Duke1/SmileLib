package xyz.openhh.biz;

import com.j256.ormlite.dao.Dao;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.List;

import xyz.openhh.bean.Department;
import xyz.openhh.util.DataHelper;

/**
 * Created by HH .
 */
public class DepartmentBiz {
    private final static Object _lock = new Object();

    private static SoftReference<DepartmentBiz> _business;


    private DepartmentBiz() {
    }

    public final static DepartmentBiz getInstance() {

        DepartmentBiz tam = null;
        synchronized (_lock) {
            if (null == _business || null == _business.get()) {
                tam = new DepartmentBiz();
                _business = new SoftReference<DepartmentBiz>(tam);
                tam = null;
            }
            tam = _business.get();
        }
        return tam;
    }


    public List<Department> getDepartmentList(DataHelper dataHelper) {
        try {
            Dao departmentDao = dataHelper.getDao(Department.class);
            List<Department> departments = departmentDao.queryForAll();
            return departments;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;

    }


    public boolean addDepartment(DataHelper dataHelper, Department department) {


        try {
            Dao departmentDao = dataHelper.getDao(Department.class);
            int rows = departmentDao.create(department);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }


}

package xyz.openhh.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import xyz.openhh.bean.ArchiveInfo;
import xyz.openhh.bean.Department;

/**
 * Created by HH .
 */
public class DataHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Wind.db";
    private static final int DATABASE_VERSION = 2;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ArchiveInfo.class);
            TableUtils.createTable(connectionSource, Department.class);

            initData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initData() {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

        initData();
    }


    public static DataHelper getHelper(Context context) {
        return new DataHelper(context);
    }
}

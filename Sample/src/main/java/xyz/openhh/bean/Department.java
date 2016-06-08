package xyz.openhh.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by HH .
 */
@DatabaseTable(tableName = "Department")
public class Department {


    public Department(String name, String abbreviation, int manager) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.manager = manager;
    }

    public Department() {
    }

    @DatabaseField(generatedId = true, columnName = "DepartmentId", canBeNull = false)
    public int departmentId;

    @DatabaseField(columnName = "Name")
    public String name;

    /**
     * 缩写
     */
    @DatabaseField(columnName = "Abbreviation")
    public String abbreviation;

    @DatabaseField( columnName = "Manager")
    public int manager;

}

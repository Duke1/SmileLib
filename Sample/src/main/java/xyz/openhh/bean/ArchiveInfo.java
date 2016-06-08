package xyz.openhh.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by HH .
 */
@DatabaseTable(tableName = "Archive")
public class ArchiveInfo {


    @DatabaseField(generatedId = true, columnName = "archiveId", canBeNull = false)
    public int archiveId;


    @DatabaseField(columnName = "name", canBeNull = false)
    public String name;
    @DatabaseField(columnName = "sex", canBeNull = false)
    public String sex;
    @DatabaseField(columnName = "nation", canBeNull = false)
    public String nation;
    @DatabaseField(columnName = "birthday", canBeNull = false)
    public Date birthday;
    @DatabaseField(columnName = "nativePlace", canBeNull = false)
    public String nativePlace;

    @DatabaseField(columnName = "familyType", canBeNull = false)
    public String familyType;

    @DatabaseField(columnName = "maritalStatus", canBeNull = false)
    public String maritalStatus;

    @DatabaseField(columnName = "degree", canBeNull = false)
    public String degree;

    @DatabaseField(columnName = "politicalLevel", canBeNull = false)
    public String politicalLevel;

    @DatabaseField(columnName = "healthStatus", canBeNull = false)
    public String healthStatus;

    @DatabaseField(columnName = "idCard", canBeNull = false)
    public String idCard;

    @DatabaseField(columnName = "houseAddress", canBeNull = false)
    public String houseAddress;

    @DatabaseField(columnName = "departmentId", canBeNull = false)
    public int departmentId;
}

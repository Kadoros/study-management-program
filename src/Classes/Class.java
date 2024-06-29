package Classes;

import java.util.Date;

public class Class {
    private int classID;
    private String className;
    private String classOwnerUserKey;
    private String classEnterCode;
    private int classMaxStu;
    private Date classExpirationDate;

    public Class(int classID, String className, String classOwnerUserKey,
            String classEnterCode, int classMaxStu, Date classExpirationDate) {
        this.classID = classID;
        this.className = className;
        this.classOwnerUserKey = classOwnerUserKey;

        this.classEnterCode = classEnterCode;
        this.classMaxStu = classMaxStu;
        this.classExpirationDate = classExpirationDate;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassOwnerUserKey() {
        return classOwnerUserKey;
    }

    public void setClassOwnerUserKey(String classOwnerUserKey) {
        this.classOwnerUserKey = classOwnerUserKey;
    }

    public String getClassEnterCode() {
        return classEnterCode;
    }

    public void setClassEnterCode(String classEnterCode) {
        this.classEnterCode = classEnterCode;
    }

    public int getClassMaxStu() {
        return classMaxStu;
    }

    public void setClassMaxStu(int classMaxStu) {
        this.classMaxStu = classMaxStu;
    }

    public Date getClassExpirationDate() {
        return classExpirationDate;
    }

    public void setClassExpirationDate(Date classExpirationDate) {
        this.classExpirationDate = classExpirationDate;
    }

    public static void forName(String string) {
    }
}

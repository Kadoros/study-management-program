package Classes;

import java.util.Date;

public class AffiliationClass extends Class {
    private String classAffiliationID;
    private String affiliationName;
    private Date affiliationExpirationDate;
    private String affiliationOwnerUserKey;
    private String affiliationOwnerUserName;

    public AffiliationClass(int classID, String className, String classOwnerUserKey, String classEnterCode,
            int classMaxStu, Date classExpirationDate, String classAffiliationID, String affiliationName,
            Date affiliationExpirationDate, String affiliationOwnerUserKey, String affiliationOwnerUserName) {
        super(classID, className, classOwnerUserKey, classEnterCode, classMaxStu, classExpirationDate);
        this.classAffiliationID = classAffiliationID;
        this.affiliationName = affiliationName;
        this.affiliationExpirationDate = affiliationExpirationDate;
        this.affiliationOwnerUserKey = affiliationOwnerUserKey;
        this.affiliationOwnerUserName = affiliationOwnerUserName;
    }

    public String getClassAffiliationID() {
        return classAffiliationID;
    }

    public void setClassAffiliationID(String classAffiliationID) {
        this.classAffiliationID = classAffiliationID;
    }

    public String getAffiliationName() {
        return affiliationName;
    }

    public void setAffiliationName(String affiliationName) {
        this.affiliationName = affiliationName;
    }

    public Date getAffiliationExpirationDate() {
        return affiliationExpirationDate;
    }

    public void setAffiliationExpirationDate(Date affiliationExpirationDate) {
        this.affiliationExpirationDate = affiliationExpirationDate;
    }

    public String getAffiliationOwnerUserKey() {
        return affiliationOwnerUserKey;
    }

    public void setAffiliationOwnerUserKey(String affiliationOwnerUserKey) {
        this.affiliationOwnerUserKey = affiliationOwnerUserKey;
    }

    public String getAffiliationOwnerUserName() {
        return affiliationOwnerUserName;
    }

    public void setAffiliationOwnerUserName(String affiliationOwnerUserName) {
        this.affiliationOwnerUserName = affiliationOwnerUserName;
    }
}

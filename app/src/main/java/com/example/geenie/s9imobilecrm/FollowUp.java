package com.example.geenie.s9imobilecrm;

public class FollowUp {

    private String followupDueDate;
    private String typeOfFollowup;
    private String followUpStatus;
    private String companyid;

    FollowUp(){}

    FollowUp(String followupDueDate, String typeOfFollowup, String followUpStatus, String companyid){

        this.followupDueDate = followupDueDate;
        this.typeOfFollowup = typeOfFollowup;
        this.followUpStatus = followUpStatus;
        this.companyid = companyid;
    }

    public String getFollowupDueDate() {
        return followupDueDate;
    }

    public void setFollowupDueDate(String followupDueDate) {
        this.followupDueDate = followupDueDate;
    }

    public String getTypeOfFollowup() {
        return typeOfFollowup;
    }

    public void setTypeOfFollowup(String typeOfFollowup) {
        this.typeOfFollowup = typeOfFollowup;
    }

    public String getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        this.followUpStatus = followUpStatus;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }
}

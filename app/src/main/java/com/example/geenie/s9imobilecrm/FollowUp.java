package com.example.geenie.s9imobilecrm;

import java.io.Serializable;

public class FollowUp implements Serializable {

    private String followupDueDate;
    private String typeOfFollowup;
    private String followUpStatus;
    private String companyid;
    private String createdBy;

    FollowUp(){}

    FollowUp(String followupDueDate, String typeOfFollowup, String followUpStatus, String companyid, String createdBy){

        this.followupDueDate = followupDueDate;
        this.typeOfFollowup = typeOfFollowup;
        this.followUpStatus = followUpStatus;
        this.companyid = companyid;
        this.createdBy = createdBy;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}

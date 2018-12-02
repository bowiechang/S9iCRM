package com.example.geenie.s9imobilecrm;

import java.io.Serializable;
import java.util.ArrayList;

public class  Task implements Serializable{

    private String title;
    private String desc;
    private String dateCreated;
    private String dueDate;
    private String status;
    private String dateCompleted;
    private String companyid;
    private String companyName;
    private String assigned_uid;
//    private HashMap<String, String> log;
    private ArrayList<String> log;

    Task(){}

    Task(String title, String desc, String dateCreated, String dueDate, String status, String dateCompleted, String companyid, String companyName, String assigned_uid, ArrayList<String> log){

        this.title = title;
        this.desc = desc;
        this.dateCreated = dateCreated;
        this.dueDate = dueDate;
        this.status = status;
        this.dateCompleted = dateCompleted;
        this.companyid = companyid;
        this.companyName = companyName;
        this.assigned_uid = assigned_uid;
        this.log = log;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getAssigned_uid() {
        return assigned_uid;
    }

    public void setAssigned_uid(String assigned_uid) {
        this.assigned_uid = assigned_uid;
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void setLog(ArrayList<String> log) {
        this.log = log;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

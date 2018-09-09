package com.example.geenie.s9imobilecrm;

import java.util.ArrayList;

public class SharedCoWorkingCompany {

    private String companyid;
    private String createdby;
    private String sharedto;
    private String status;
    private ArrayList<String> log;



    public SharedCoWorkingCompany(){}

    public SharedCoWorkingCompany(String companyid ,String createdby, String sharedto, String status, ArrayList<String> log){

        this.companyid = companyid;
        this.createdby = createdby;
        this.sharedto = sharedto;
        this.status = status;
        this.log = log;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getSharedto() {
        return sharedto;
    }

    public void setSharedto(String sharedto) {
        this.sharedto = sharedto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void setLog(ArrayList<String> log) {
        this.log = log;
    }
}

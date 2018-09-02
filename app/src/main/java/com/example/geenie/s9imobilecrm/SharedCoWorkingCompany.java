package com.example.geenie.s9imobilecrm;

import java.util.ArrayList;

public class SharedCoWorkingCompany {

    private Company company;
    private ArrayList<Copier> copierArrayList;
    private ArrayList<Appointment> appointmentArrayList;
    private ArrayList<FollowUp> followUpArrayList;
    private ArrayList<Contact> contactArrayList;
    private String createBy;
    private String sharedwith;
    private String companyid;


    public SharedCoWorkingCompany(){}

    public SharedCoWorkingCompany(Company company, ArrayList<Copier> copierArrayList, ArrayList<Appointment> appointmentArrayList,
                                  ArrayList<FollowUp> followUpArrayList, ArrayList<Contact> contactArrayList, String createBy, String sharedwith, String companyid){

        this.company = company;
        this.copierArrayList = copierArrayList;
        this.appointmentArrayList = appointmentArrayList;
        this.followUpArrayList = followUpArrayList;
        this.contactArrayList = contactArrayList;
        this.createBy = createBy;
        this.sharedwith = sharedwith;
        this.companyid = companyid;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ArrayList<Copier> getCopierArrayList() {
        return copierArrayList;
    }

    public void setCopierArrayList(ArrayList<Copier> copierArrayList) {
        this.copierArrayList = copierArrayList;
    }

    public ArrayList<Appointment> getAppointmentArrayList() {
        return appointmentArrayList;
    }

    public void setAppointmentArrayList(ArrayList<Appointment> appointmentArrayList) {
        this.appointmentArrayList = appointmentArrayList;
    }

    public ArrayList<FollowUp> getFollowUpArrayList() {
        return followUpArrayList;
    }

    public void setFollowUpArrayList(ArrayList<FollowUp> followUpArrayList) {
        this.followUpArrayList = followUpArrayList;
    }

    public ArrayList<Contact> getContactArrayList() {
        return contactArrayList;
    }

    public void setContactArrayList(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getSharedwith() {
        return sharedwith;
    }

    public void setSharedwith(String sharedwith) {
        this.sharedwith = sharedwith;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }
}

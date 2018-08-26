package com.example.geenie.s9imobilecrm;

public class Appointment {

    private String name;
    private String time;
    private String date;
    private String locationName;
    private String locationAddress;
    private String comments;
    private String createby;
    private String dateCreated;
    private String contact_id;
    private String company_id;

    public Appointment(){}

    public Appointment(String name, String time, String date, String locationName, String locationAddress, String comments, String createby, String dateCreated, String contact_id, String company_id){

        this.name = name;
        this.time = time;
        this.date = date;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.comments = comments;
        this.createby = createby;
        this.dateCreated = dateCreated;
        this.contact_id = contact_id;
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String location) {
        this.locationName = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}

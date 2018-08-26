package com.example.geenie.s9imobilecrm;

public class Contact {

    private String name;
    private String title;
    private String mobileNumber;
    private String officeNumber;
    private String email;
    private Boolean ic;
    private String company_id;

    public Contact(){}

    public Contact(String name, String title, String mobileNumber, String officeNumber, String email, Boolean ic, String company_id){
        this.name = name;
        this.title = title;
        this.mobileNumber = mobileNumber;
        this.officeNumber = officeNumber;
        this.email = email;
        this.ic = ic;
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getIc() {
        return ic;
    }

    public void setIc(Boolean ic) {
        this.ic = ic;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

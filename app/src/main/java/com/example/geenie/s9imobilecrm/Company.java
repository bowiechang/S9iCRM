package com.example.geenie.s9imobilecrm;

import java.io.Serializable;

public class Company implements Serializable{

    private String name;
    private String address;
    private String postalCode;
    private String unitNo;
    private String officeTel;
    private String industry;
    private String companyLackOf;
    private String priorityLevel;
    private String comment;
    private String createBy;
    private String dateCreated;
    private int numberOfTimesCalled;

    public Company(){}

    public Company(String name, String address, String postalCode, String unitNo, String officeTel, String industry,
                   String companyLackOf, String priorityLevel, String comment, String createBy, String dateCreated, int numberOfTimesCalled){

        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.unitNo = unitNo;
        this.officeTel = officeTel;
        this.industry = industry;
        this.companyLackOf = companyLackOf;
        this.priorityLevel = priorityLevel;
        this.comment = comment;
        this.createBy = createBy;
        this.dateCreated = dateCreated;
        this.numberOfTimesCalled = numberOfTimesCalled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyLackOf() {
        return companyLackOf;
    }

    public void setCompanyLackOf(String companyLackOf) {
        this.companyLackOf = companyLackOf;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getNumberOfTimesCalled() {
        return numberOfTimesCalled;
    }

    public void setNumberOfTimesCalled(int numberOfTimesCalled) {
        this.numberOfTimesCalled = numberOfTimesCalled;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}

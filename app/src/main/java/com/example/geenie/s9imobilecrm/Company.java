package com.example.geenie.s9imobilecrm;

public class Company {

    private String name;
    private String postalCode;
    private String unitNo;
    private String officeTel;
    private String industry;
    private String companyLackOf;
    private String priorityLevel;
    private String comment;

    public Company(){}

    public Company(String name, String postalCode, String unitNo, String officeTel, String industry,
                   String companyLackOf, String priorityLevel, String comment){

        this.name = name;
        this.postalCode = postalCode;
        this.unitNo = unitNo;
        this.officeTel = officeTel;
        this.industry = industry;
        this.companyLackOf = companyLackOf;
        this.priorityLevel = priorityLevel;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

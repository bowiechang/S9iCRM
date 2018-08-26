package com.example.geenie.s9imobilecrm;

public class Copier {

    private String brand;
    private String model;
    private String age;
    private String problemfaced;
    private String rentedOrPurchased;
    private int contractLength;
    private String contractStartDate;
    private String contractExpiryDate;
    private String contractMonthlyPayment;
    private String contractFinalPayment;
    private String company_id;

    public Copier(){}

    public Copier(String brand, String model, String age, String problemfaced, String rentedOrPurchased, int contractLength,
                  String contractStartDate, String contractExpiryDate, String contractMonthlyPayment, String contractFinalPayment, String company_id){
        this.brand = brand;
        this.model = model;
        this.age = age;
        this.problemfaced = problemfaced;
        this.rentedOrPurchased = rentedOrPurchased;
        this.contractLength = contractLength;
        this.contractStartDate = contractStartDate;
        this.contractExpiryDate = contractExpiryDate;
        this.contractMonthlyPayment = contractMonthlyPayment;
        this.contractFinalPayment = contractFinalPayment;
        this.company_id = company_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProblemfaced() {
        return problemfaced;
    }

    public void setProblemfaced(String problemfaced) {
        this.problemfaced = problemfaced;
    }

    public String getRentedOrPurchased() {
        return rentedOrPurchased;
    }

    public void setRentedOrPurchased(String rentedOrPurchased) {
        this.rentedOrPurchased = rentedOrPurchased;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(int contractLength) {
        this.contractLength = contractLength;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractExpiryDate() {
        return contractExpiryDate;
    }

    public void setContractExpiryDate(String contractExpiryDate) {
        this.contractExpiryDate = contractExpiryDate;
    }

    public String getContractMonthlyPayment() {
        return contractMonthlyPayment;
    }

    public void setContractMonthlyPayment(String contractMonthlyPayment) {
        this.contractMonthlyPayment = contractMonthlyPayment;
    }

    public String getContractFinalPayment() {
        return contractFinalPayment;
    }

    public void setContractFinalPayment(String contractFinalPayment) {
        this.contractFinalPayment = contractFinalPayment;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}

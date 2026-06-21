package org.northernarc.weeklyassignment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {
    @Id
    private String applicationId;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String lenderName;
    @Column(nullable = false)
    private String loanType;
    @Column(nullable = false)
    private double loanAmount;
    @Column(nullable = false)
    private int creditScore;

    public LoanApplication(){

    }

    public LoanApplication(String applicationId, String customerName,String lenderName,String loanType,
                           double loanAmount, int creditScore) {
        this.applicationId=applicationId;
        this.customerName = customerName;
        this.lenderName = lenderName;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.creditScore = creditScore;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getLenderName() {
        return lenderName;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public int getCreditScore() {
        return creditScore;
    }
}

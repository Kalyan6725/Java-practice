class Loan {
    private int loanId;
    private int loanAmount;
    private int loanInterest;
    private int loanTenure;
    private String loanStatus;
    private String loanType;

    Loan(int loanId, int loanAmount, int loanInterest, int loanTenure, String loanStatus, String loanType) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.loanInterest = loanInterest;
        this.loanTenure = loanTenure;
        this.loanStatus = loanStatus;
        this.loanType = loanType;
    }

    public int getLoanId() {
        return loanId;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getLoanInterest() {
        return loanInterest;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public String getLoanType() {
        return loanType;
    }

    void setLoanInterest(int loanInterest) {
        this.loanInterest = loanInterest;
    }

    @Override
    public String toString() {
        return "{ Loan ID: " + loanId + " Loan Amount: " + loanAmount + " Loan Interest: " + loanInterest + "% Loan Tenure: " + loanTenure + " years Loan Status: " + loanStatus + " Loan Type: " + loanType + " }";
    }
}
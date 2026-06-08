class LoanAccount {

    private double loanAmount;
    private String customerName;

    public void setLoanAmount(double loanAmount) {
        if(loanAmount > 0) {
            this.loanAmount = loanAmount;
        }
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
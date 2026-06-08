class LoanMain {
    public static void main(String[] args) {
        LoanAccount loan = new LoanAccount();
        loan.setCustomerName("John Doe");
        loan.setLoanAmount(10000);
        

        System.out.println("Customer Name: " + loan.getCustomerName());
        System.out.println("Loan Amount: " + loan.getLoanAmount());
    }
}
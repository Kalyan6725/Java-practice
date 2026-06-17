class ExpenseManager {
    PaymentService ps;
    // public ExpenseManager(PaymentService ps) {
    //     this.ps = ps;
    // }
    public void setPaymentService(PaymentService ps) {
        this.ps = ps;
    }
    public void payElectricityBill(double amount) {
        System.out.println("Paying electricity bill of amount: " + amount);
        ps.pay(amount);
        System.out.println("Electricity bill paid successfully.");
    }

    public void payWaterBill(double amount) {
        System.out.println("Paying water bill of amount: " + amount);
        ps.pay(amount);
        System.out.println("Water bill paid successfully.");
    }

    public void payGasBill(double amount) {
        System.out.println("Paying gas bill of amount: " + amount);
        ps.pay(amount);
        System.out.println("Gas bill paid successfully.");
    }
}
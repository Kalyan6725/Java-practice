class ExpenseManager {
    PaymentService ps;

    NotificationService ns;
    public ExpenseManager(PaymentService ps,NotificationService ns){
        this.ps = ps;
        this.ns = ns;

    }
    public void payElectricityBill(double amount) {
        System.out.println("Paying electricity bill of amount: " + amount);
        ps.pay(amount);
        ns.notify();
        System.out.println("Electricity bill paid successfully.");
    }

    public void payWaterBill(double amount) {
        System.out.println("Paying water bill of amount: " + amount);
        ps.pay(amount);
        ns.notify();
        System.out.println("Water bill paid successfully.");
    }

    public void payGasBill(double amount) {
        System.out.println("Paying gas bill of amount: " + amount);
        ps.pay(amount);
        ns.notify();
        System.out.println("Gas bill paid successfully.");
    }
}
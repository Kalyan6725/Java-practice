class DebitCard implements PaymentService {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Debit Card");
    }
}
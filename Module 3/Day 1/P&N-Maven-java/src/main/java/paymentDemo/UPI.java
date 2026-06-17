package paymentDemo;

public class UPI implements PaymentService{
    @Override
    public void pay(int amount) {
        System.out.println("UPI payment: " + amount);
    }
}

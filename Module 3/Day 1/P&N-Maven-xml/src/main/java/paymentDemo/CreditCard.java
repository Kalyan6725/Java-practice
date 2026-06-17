package paymentDemo;

public class CreditCard implements PaymentService{
    @Override
    public void pay(int amount) {
        System.out.println("Credit Card: " + amount);
    }
}

package paymentDemo;

public class DebitCard implements PaymentService{
    @Override
    public void pay(int amount) {
        System.out.println("DebitCard Payment: " + amount);
    }
}

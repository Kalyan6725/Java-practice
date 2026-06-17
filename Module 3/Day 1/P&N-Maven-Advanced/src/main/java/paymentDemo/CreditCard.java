package paymentDemo; 

import org.springframework.stereotype.Component;

@Component("Credit")
public class CreditCard implements PaymentService{
    @Override
    public void pay(int amount) {
        System.out.println("Credit Card: " + amount);
    }
}

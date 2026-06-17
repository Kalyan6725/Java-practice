package paymentDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
@Component
public class ExpenceManager {
    @Autowired
    @Qualifier("Upi")
    PaymentService paymentService;
    @Autowired
    @Qualifier("Email")
    NotificationService notificationService;

    public ExpenceManager(@Qualifier("Upi") PaymentService paymentService,
                          @Qualifier("Email") NotificationService notificationService) {
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public void payHouseRent(){
        paymentService.pay(1000);
        notificationService.send(1000);
    }
    public void payGas(){
        paymentService.pay(500);
        notificationService.send(500);
    }
    public void payElectricity(){
        paymentService.pay(800);
        notificationService.send(800);
    }
}

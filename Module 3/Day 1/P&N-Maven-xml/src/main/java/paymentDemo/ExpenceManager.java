package paymentDemo;

import java.sql.SQLOutput;

public class ExpenceManager {
    PaymentService paymentService;
    NotificationService notificationService;

    public ExpenceManager(PaymentService paymentService, NotificationService notificationService) {
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

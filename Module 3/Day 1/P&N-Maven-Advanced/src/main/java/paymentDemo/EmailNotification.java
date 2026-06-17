package paymentDemo;

import org.springframework.stereotype.Component;

@Component("Email")
public class EmailNotification implements NotificationService{
    @Override
    public void send(int amount){
        System.out.println("Email msg ,paid:"+amount);
    }
}

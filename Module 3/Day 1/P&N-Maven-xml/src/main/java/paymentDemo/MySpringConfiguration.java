package paymentDemo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySpringConfiguration {
    @Bean("Credit")
    public PaymentService paymentServiceCredit(){
        return new CreditCard();
    }
    @Bean("Debit")
    public PaymentService paymentServiceDebit(){
        return new DebitCard();
    }
    @Bean("Upi")
    public PaymentService paymentServiceUpi(){
        return new UPI();
    }
    @Bean("Email")
    public NotificationService notificationServiceEmail(){
        return new EmailNotification();
    }
    @Bean("WhatsApp")
    public NotificationService notificationServiceWhatsApp(){
        return new WhatsAppNotification();
    }
    @Bean
    public ExpenceManager expenceManager(@Qualifier("Upi") PaymentService paymentService,@Qualifier("Email") NotificationService notificationService){
        return new ExpenceManager(paymentService,notificationService);
    }
}

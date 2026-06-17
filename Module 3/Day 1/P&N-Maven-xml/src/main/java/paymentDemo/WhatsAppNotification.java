package paymentDemo;

public class WhatsAppNotification implements NotificationService{
    @Override
    public void send(int amount){
        System.out.println("Whatsapp msg ,amt:"+amount);
    }
}

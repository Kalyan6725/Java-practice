package paymentDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;
@Configuration
@ComponentScan(basePackages ="paymentDemo")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context=new AnnotationConfigApplicationContext(Main.class);
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter your choice: 1.CreditCard 2.DebitCard 3.UPI ,default is Upi");
        int choice=sc.nextInt();
        PaymentService paymentService = context.getBean("Upi", PaymentService.class);
        switch(choice){
            case 1:
                paymentService=context.getBean("Credit",PaymentService.class);
                break;
            case 2:
                paymentService=context.getBean("Debit",PaymentService.class);
                break;
            case 3:
                paymentService=context.getBean("Upi",PaymentService.class);
                break;
            default:
                System.out.println("Invalid choice ,default UPI");
        }

        NotificationService notificationService = context.getBean("Email", NotificationService.class);
        System.out.println("Enter notification choice:1.Email,2.WhatsApp");
        int msgChoice=sc.nextInt();
        switch(msgChoice){
            case 1:
                notificationService=context.getBean("Email",NotificationService.class);
                break;
            case 2:
                notificationService=context.getBean("WhatsApp",NotificationService.class);
                break;
            
            default:
                System.out.println("Invalid choice ");
        }
        // ExpenceManager expenceManager=context.getBean(ExpenceManager.class);
        ExpenceManager expenceManager=new ExpenceManager(paymentService,notificationService);
        expenceManager.payHouseRent();
        expenceManager.payGas();
        expenceManager.payElectricity();

    }
}

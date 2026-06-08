import java.util.Scanner;
class MsgMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice:\n 1:Whatsup \n 2:Email \n 3:Text \n");
        int choice = sc.nextInt();

        SimpleMsg msg;
        switch (choice) {
            case 1:
                msg = new WhatsupMsg();
                ((WhatsupMsg) msg).sendWhatsupMsg();
                break;
            case 2:
                msg = new EmailMsg();
                ((EmailMsg) msg).sendEmailMsg();
                break;
            case 3:
                msg = new TextMsg();
                ((TextMsg) msg).sendTextMsg();
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }
        msg.sendMsg();
    }
}
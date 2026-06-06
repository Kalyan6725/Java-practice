import java.util.Scanner;
class SimpleMsg {
    protected String msg;

    SimpleMsg() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your message:");
        msg = sc.nextLine();
    }
    

    void sendMsg(){
        System.out.println("Message sent: " + msg);
    }
}
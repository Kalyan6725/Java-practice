import java.util.Scanner;
class MainPayments {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice:\n 1:Debit Card \n 2:Credit Card \n 3:UPI \n");
        int choice = sc.nextInt();
        Payments payment;
        switch (choice) {
            case 1:
                payment = new Debit();
                break;
            case 2:
                payment = new Credit();
                break;
            case 3:
                sc.nextLine(); // why is this needed? // This is needed to consume the newline character left by nextInt()
                System.out.println("Enter your UPI ID:");
                String upiId = sc.nextLine();
                payment = new Upi(upiId);
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }
        payment.pay();
    }
}
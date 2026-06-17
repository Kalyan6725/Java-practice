import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        while (true){
            
            System.out.println("Enter your payment method (1 for Debit Card, 2 for Credit Card, 3 for UPI):");
            Scanner sc = new Scanner(System.in);
            String paymentMethod = sc.nextLine();
            PaymentService ps=PaymentFactory.getPaymentService(paymentMethod);
            ExpenseManager expenseManager = new ExpenseManager();
            expenseManager.setPaymentService(ps);
            System.out.println("Enter choice of Bill:\n1.Electricity Bill\n2.Water Bill\n3.Gas Bill\n4.Exit");
            int choice = sc.nextInt();
            if (choice == 4) {
                System.out.println("Exiting...");
                break;
            }
            System.out.println("Enter the amount:");
            double amount = sc.nextDouble();
            switch (choice) {
                case 1:
                    expenseManager.payElectricityBill(amount);
                    break;
                case 2:
                    expenseManager.payWaterBill(amount);
                    break;
                case 3:
                    expenseManager.payGasBill(amount);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
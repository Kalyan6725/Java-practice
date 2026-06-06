import java.util.Scanner;
class MainBank
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = null;
        while (true) {
            System.out.println("Enter your choice:\n 1:Open Account \n 2:Deposit \n 3:Withdraw \n 4.Check Balance \n 5.Exit \n");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    System.out.println("Enter the Acc holder name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter the initial balance:");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine();
                    account = new BankAccount("ACC123", initialBalance);
                    System.out.println("Account opened for " + name + " with initial balance: " + initialBalance);
                    break;
                case 2:
                    if (account == null) {
                        System.out.println("Please open an account first.");
                        break;
                    }
                    System.out.println("Enter the amount to deposit:");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();
                    account.deposit(depositAmount);
                    break;
                case 3:
                    if (account == null) {
                        System.out.println("Please open an account first.");
                        break;
                    }
                    System.out.println("Enter the amount to withdraw:");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();
                    account.withdraw(withdrawAmount);
                    break;
                case 4:
                    if (account == null) {
                        System.out.println("Please open an account first.");
                        break;
                    }
                    System.out.println("Current balance: " + account.getBalance());
                    break;
                case 5:
                    System.out.println("Exit");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
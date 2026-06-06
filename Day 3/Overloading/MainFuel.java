import java.util.Scanner;
class MainFuel{
    public static void main(String[] args) {
        Fuel fuel =new Fuel(50,10,15);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option:\n1. Fill Fuel\n2. Check Fuel Level\n3. Drive\n4. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter amount of fuel to fill:");
                    double amount = scanner.nextDouble();
                    fuel.fillFuel(amount);
                    break;
                case 2:
                    fuel.checkFuel();
                    break;
                case 3:
                    System.out.println("Enter distance to drive:");
                    double distance = scanner.nextDouble();
                    fuel.drive(distance);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
}
}
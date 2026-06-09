import java.util.Scanner;
class ManuallyThrow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name =sc.nextLine();
        try {
        if (!name.equals("kalyan") && !name.equals("samineni")) {
            
            throw new NameNotFound("Invalid name entered. Only 'kalyan' and 'samineni' are allowed.");
        }
        else {
            System.out.println("Welcome, " + name + "!");
        }
        }
         catch (NameNotFound e) {
            System.out.println("Caught an exception: " + e.getMessage());
        } finally {
            sc.close();
        }
    } 
}
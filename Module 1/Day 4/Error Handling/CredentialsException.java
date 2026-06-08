//Username
//Password
//Invalid credentials exception
import java.util.Scanner;
class CredentialsException extends Exception {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        try {
            if (!username.equals("admin") || !password.equals("password123")) {
                throw new NameNotFound("Invalid username or password.");
            } else {
                System.out.println("Login successful!");
            }
        } catch (NameNotFound e) {
            System.out.println("Invalid credentials. Please try again."+e.getMessage());
        } finally {
            sc.close();
        }
    }
}
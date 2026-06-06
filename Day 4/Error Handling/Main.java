import java.util.InputMismatchException;

import java.util.Scanner;
class Main {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        try {
            String str = null;
            System.out.println(str.length()); // This will throw NullPointerException
            System.out.print("Enter a number: ");
            int a = sc.nextInt();
            System.out.print("Enter another number: ");
            int b= sc.nextInt();
            double result = (double) a / b;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Cannot divide by zero");
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }finally {
            System.out.println("This will always be executed");
            sc.close();
        }
    }
}
import java.util.Scanner;
public class PosNeg {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int a = sc.nextInt();

        if(a==0){
            System.out.println("a is neither negative nor positive: " + a);
        }
        else{
            if (a > 0) {
                System.out.println("a is +ve: " + a);
            } 
            else {
                System.out.println("a is -ve: " + a);
            }
        }
    }
}

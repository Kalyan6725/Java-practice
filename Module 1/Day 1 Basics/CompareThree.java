public class CompareThree {
    public static void main(String[] args) {

        int a = 10;
        int b = 25;
        int c = 15;

        if (a >= b && a >= c) {
            System.out.println("a is greatest: " + a);
        } 
        else if (b >= a && b >= c) {
            System.out.println("b is greatest: " + b);
        } 
        else {
            System.out.println("c is greatest: " + c);
        }
    }
}

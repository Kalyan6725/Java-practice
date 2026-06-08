import java.util.Scanner;
class EvenNumbers{
    public static void func(int num){
        for(int i=0;i<num;i++){
            if(i%2==0){
                System.out.println(i );
            }
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("enter a positive number:");
        int num=sc.nextInt();
        func(num);
    }
}
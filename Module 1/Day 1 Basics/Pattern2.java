import java.util.Scanner;
class Pattern2{
    public static void func(int num){
        int cnt=0;
        for(int i=1;i<=num;i++){
            for(int j=num-i;j>0;j--){
                System.out.print(" ");
            }
            for(int k=0;k<2*i-1;k++){
                if(k==0 || k==2*i-2){
                    System.out.print("*");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("enter a positive number:");
        int num=sc.nextInt();
        func(num);
    }
}
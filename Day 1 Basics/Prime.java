import java.util.Scanner;
class Prime{
    public static void func(int num){
        int cnt=0;
        for(int i=1;i<=num;i++){
            if(num%i == 0){
                cnt++;
            }
        }
        if(cnt==2){
            System.out.println("Prime");
        }
        else{
            System.out.println("not Prime");
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("enter a positive number:");
        int num=sc.nextInt();
        func(num);
    }
}
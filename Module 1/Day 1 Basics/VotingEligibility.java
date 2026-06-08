import java.util.Scanner;
class VotingEligibility{
    public static string Eligibility(int age){
        if(age>18)return "Eligible";
        else return "Not Eligible";
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Age,Marks range should be > 0:");
        int age=sc.nextInt();
        if(age>150 || age<0){
            System.out.println("age range should be 0 to 150");
        }
        else{
            string[] result=Eligibility(age);
            System.out.println(result+"for Voting");
        }
    }
}
import java.util.Scanner;
class StudentGrade{
    public static char Grade(int marks){
        if(marks>90)return 'A';
        else if (marks>80)return 'B';
        else if(marks>70)return 'C';
        else if(marks>60)return 'D';
        else if(marks>50)return 'P';
        else return 'F';
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Marks,Marks range should be 0 to 100:");
        int marks=sc.nextInt();
        if(marks>100 || marks<0){
            System.out.println("Marks range should be 0 to 100");
        }
        else{
            char result=Grade(marks);
            System.out.println("Grade:"+result);
        }
    }
}
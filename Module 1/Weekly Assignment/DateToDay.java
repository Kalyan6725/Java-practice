import java.util.Scanner;

public class DateToDay {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter month 1 to 12:");
        int month = sc.nextInt();
        System.out.println("Enter date 1 to 31:");
        int date = sc.nextInt();

        int totalDays = date - 1;

        switch(month) {
            case 12: totalDays += 30;
            case 11: totalDays += 31;
            case 10: totalDays += 30;
            case 9:  totalDays += 31;
            case 8:  totalDays += 31;
            case 7:  totalDays += 30;
            case 6:  totalDays += 31;
            case 5:  totalDays += 30;
            case 4:  totalDays += 31;
            case 3:  totalDays += 28;
            case 2:  totalDays += 31;
            case 1:  break;
        }

        int day = (4 + totalDays) % 7; // Thursday = 4

        switch(day) {
            case 0: System.out.println("Sunday"); break;
            case 1: System.out.println("Monday"); break;
            case 2: System.out.println("Tuesday"); break;
            case 3: System.out.println("Wednesday"); break;
            case 4: System.out.println("Thursday"); break;
            case 5: System.out.println("Friday"); break;
            case 6: System.out.println("Saturday"); break;
        }
    }
}
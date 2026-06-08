import java.util.Arrays;
import java.util.Comparator;

class MainDOB {
    public static void main(String[] args) {
        Child[] c = {
            new Child("John", "Doe", "01-01-2000"),
            new Child("Jane", "Smith", "15-01-1998"),
            new Child("Bob", "Johnson", "20-01-2000"),
            new Child("Alice", "Williams", "30-12-1999"),
            new Child("Charlie", "Brown", "25-07-2000")
        };
        Arrays.sort(c, new Comparator<Child>(){
            public int compare(Child c1,Child c2){
                String[] c1DOB = (c1.getDOB().split("-"));
                int year1= Integer.parseInt(c1DOB[2]);
                int month1= Integer.parseInt(c1DOB[1]);
                int day1= Integer.parseInt(c1DOB[0]);

                String[] c2DOB = (c2.getDOB().split("-"));
                int year2= Integer.parseInt(c2DOB[2]);
                int month2= Integer.parseInt(c2DOB[1]);
                int day2= Integer.parseInt(c2DOB[0]);

                if(year1!=year2){
                    return Integer.compare(year1,year2); //used instead of year1-year2 
                }
                else if(month1!=month2){
                    return Integer.compare(month1,month2);
                }
                else{
                    return Integer.compare(day1,day2);
                }
            }
        });
        System.out.println(Arrays.toString(c));
    }
}
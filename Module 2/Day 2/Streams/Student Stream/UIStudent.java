import java.util.Scanner;
import java.util.List;

class UIStudent {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDaoImpl();
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice != 11) {
            System.out.println("1. Add Student");
            System.out.println("2. Get All Students");
            System.out.println("3. top Maths Marks");
            System.out.println("4. top Physics Marks");
            System.out.println("5. top Chemistry Marks");
            System.out.println("6. avg Maths Marks");
            System.out.println("7. avg Physics Marks");
            System.out.println("8. avg Chemistry Marks");
            System.out.println("9. Students above Average Physics Marks");
            System.out.println("10. Topper");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Student Name:");
                    sc.nextLine(); // Consume the newline character
                    String name = sc.nextLine();
                    System.out.println("Enter Maths Marks:");
                    int MATHSmarks = sc.nextInt();
                    System.out.println("Enter Physics Marks:");
                    int PHYmarks = sc.nextInt();
                    System.out.println("Enter Chemistry Marks:");
                    int CHEmarks = sc.nextInt();
                    System.out.println("Enter History Marks:");
                    int HistoryMarks = sc.nextInt();
                    System.out.println("Enter Geography Marks:");
                    int Geographymarks = sc.nextInt();
                    
                    studentDao.addStudent(new Student(name, PHYmarks, CHEmarks, MATHSmarks, HistoryMarks, Geographymarks));
                    break;
                case 2:
                    List<Student> allStudents = studentDao.getAllStudents();
                    System.out.println(allStudents);
                    break;
                case 3:
                    System.out.println("Student with Top Maths Marks:");
                    System.out.println(studentDao.maxMarksMaths());
                    break;
                case 4:
                    System.out.println("Student with Top Physics Marks:");
                    System.out.println(studentDao.maxMarksPhy());
                    break;
                case 5:
                    System.out.println("Student with Top Chemistry Marks:");
                    System.out.println(studentDao.maxMarksChe());
                    break;
                case 6:
                    System.out.println("Average Maths Marks:");
                    System.out.println(studentDao.avgMarksMaths());
                    break;
                case 7:
                    System.out.println("Average Physics Marks:");
                    System.out.println(studentDao.avgMarksPhy());
                    break;
                case 8:
                    System.out.println("Average Chemistry Marks:");
                    System.out.println(studentDao.avgMarksChe());
                    break;
                case 9:
                    System.out.println(studentDao.aboveAvgPhy());
                    break;
                case 10:
                    System.out.println(studentDao.topper());
                    break;
                case 11:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
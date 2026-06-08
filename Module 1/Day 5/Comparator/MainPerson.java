import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class MainPerson{
    public static void main(String[] args) {

        class AgeAscComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getAge() - p2.getAge(); 
            }
        }

        class AgeDescComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p2.getAge() - p1.getAge(); 
            }
        }

        class FnameAscComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getFname().compareTo(p2.getFname()); 
            }
        }

        class LnameAscComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getLname().compareTo(p2.getLname()); 
            }
        }

        class FnameDescComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p2.getFname().compareTo(p1.getFname()); 
            }
        }

        class LnameDescComparator implements Comparator<Person> {
            @Override
            public int compare(Person p1, Person p2) {
                return p2.getLname().compareTo(p1.getLname()); 
            }
        }

        
        Person[] p = new Person[5];
        p[0] = new Person("John", "Doe", 30);
        // p[0].display();

        p[1] = new Person("Jane", "Smith", 25);
        // p[1].display();
        p[2] = new Person("Bob", "Johnson", 35);
        // p[2].display();
        p[3] = new Person("Alice", "Williams", 28);
        // p[3].display();
        p[4] = new Person("Charlie", "Brown", 40);
        // p[4].display();
        System.out.println(Arrays.toString(p)); // This will print the contents of the array

        System.out.println("Enter 1 for sort with age in ASC\nEnter 2 for sort with age in DESC\nEnter 3 for sort with first name in ASC\n Enter 4 for sort with last name in ASC\nEnter 5 for sort with first name in DESC\n Enter 6 for sort with last name in DESC");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                // Implement sorting by age in ascending order 
                Arrays.sort(p, new AgeAscComparator());  
                break;
            case 2:
                // Implement sorting by age in descending order
                Arrays.sort(p, new AgeDescComparator());
                break;
            case 3:
                // Implement sorting by first name in ascending order
                Arrays.sort(p, new FnameAscComparator());
                break;
            case 4:
                // Implement sorting by last name in ascending order
                Arrays.sort(p, new LnameAscComparator());
                break;
            case 5:
                // Implement sorting by first name in descending order
                Arrays.sort(p, new FnameDescComparator());
                break;
            case 6:
                // Implement sorting by last name in descending order
                Arrays.sort(p, new LnameDescComparator());
                break;
            default:
                System.out.println("Invalid choice");
        }
        System.out.println("After sorting the array:");
        System.out.println(Arrays.toString(p)); // This will print the contents of the array after sorting
    }
}
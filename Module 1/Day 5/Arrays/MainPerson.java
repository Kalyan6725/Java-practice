import java.util.Arrays;

class MainPerson{
    public static void main(String[] args) {
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

        System.out.println("Enter 1 for sort with age\nEnter 2 for sort with first name\n Enter 3 for sort with last name");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                Arrays.sort(p); // This will sort the array based on the compareTo method defined in the Person class
                break;
            case 2:
                // Implement sorting by first name
                Arrays.sort(p, (a, b) -> b.getFname().compareTo(a.getFname()));
                break;
            case 3:
                // Implement sorting by last name
                Arrays.sort(p, (a, b) -> b.getLname().compareTo(a.getLname()));
                break;
            default:
                System.out.println("Invalid choice");
        }
        System.out.println("After sorting the array:");
        System.out.println(Arrays.toString(p)); // This will print the contents of the array after sorting
    }
}
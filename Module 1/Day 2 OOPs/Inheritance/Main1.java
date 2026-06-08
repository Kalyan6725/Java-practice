class Main1{
    public static void main(String[] args) {
        //Employee emp1 = new Employee();
        //Employee emp1 = new Employee("John", "Doe", 30, 101, 50000.0);
        // emp1.setFname("John"); // Inherited method from Person class
        // emp1.setLname("Doe"); // Inherited method from Person class
        // emp1.setAge(30); // Inherited method from Person class
        // emp1.setId(101);// Employee class method
        // emp1.setSalary(50000.0);  // Employee class method
        // emp1.displayInfo(); // Employee class method
        // emp1.work(); // Employee class method
        // emp1.eat(); // Inherited method from Person class

        //Student student1 = new Student("Kal", "Sams", 21, 102, "Computer Science");
        // student1.setFname("Kal"); // Inherited method from Person class
        // student1.setLname("Sams"); // Inherited method from Person class
        // student1.setAge(21); // Inherited method from Person class
        // student1.setRollNo(102); // Student class method
        // student1.setCourse("Computer Science"); // Student class method
        // student1.displayStudentInfo(); // Student class method
        // student1.walk(); // Inherited method from Person class

        // //upcasting
        // Person emp = new Employee("Alice", "Smith", 25, 102, 60000.0); // Upcasting Employee to Person
        // //parent reference can only access methods of parent class
        // //variable of parent class can refer to child class object but it can only access the methods of parent class
        // emp.setFname("Alice");
        // emp.setLname("Smith");
        // emp.setAge(25);
        // //emp.setId(102); // This will cause a compile-time error because setId is not defined in Person class
        // emp.displayInfo(); // This will call the displayInfo method from Person class

        // //downcasting
        // ((Employee) emp).setSalary(60000.0); // Downcasting Person to Employee
        // ((Employee) emp).displayInfo(); // This will call the displayInfo method from Employee class

        Person p=new Manager("John", "Doe", 30, 101, 50000.0, "IT"); // Upcasting Manager to Person
        p.displayInfo(); // This will call the displayInfo method from Employee class(Overridden method)
        p.eat(); // This will call the eat method from Person class because Manager does not override it
        //p.manage(); // This will cause a compile-time error because manage is not defined in Person class
        ((Employee) p).work(); // This will call the work method from Employee class due to downcasting

    }
}
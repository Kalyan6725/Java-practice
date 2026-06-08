class Employee extends Person {
    private int id;
    private double salary;

    public Employee(String fname, String lname, int age, int id, double salary) {
        super(fname, lname, age); // Call the constructor of the Person class
        this.id = id;
        this.salary = salary;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public double getSalary() {
        return salary;
    }
    public void work() {
        System.out.println(this.fname + " " + this.lname + " is working.");
    }

    public void displayInfo() {
        super.displayInfo(); // Call the displayInfo method from Person class
        System.out.println("Employee ID: " + getId());
        System.out.println("Employee Salary: " + getSalary());
    }
}
class Manager extends Employee {
    private String department;

    public Manager(String fname, String lname, int age, int id, double salary, String department) {
        super(fname, lname, age, id, salary); // Call the constructor of the Employee class
        this.department = department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
}
class Person {
    String fname;
    String lname;
    int age;

    public Person(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname; 
        this.age = age;
    }

    public int hashCode() {
        return fname.hashCode() + lname.hashCode();
    }

    public void displayInfo() {
        System.out.println("Name: " + fname + " " + lname);
        System.out.println("Age: " + age);
    }
    public boolean equals(Object obj) {
        Person p=(Person) obj;
        return this.fname.equals(p.fname) && this.lname.equals(p.lname);
    }
}
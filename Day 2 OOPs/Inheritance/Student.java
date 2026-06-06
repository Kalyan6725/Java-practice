class Student extends Person {
    int rollNo;
    String course;

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }
    public int getRollNo() {
        return rollNo;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public String getCourse() {
        return course;
    }


    void displayStudentInfo() {
        super.displayInfo(); // Call the displayInfo method from Person class
        System.out.println("Roll No: " + getRollNo());
        System.out.println("Course: " + getCourse());
    }
}
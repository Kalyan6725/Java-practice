class Person implements Comparable<Person>{
    private String fname;
    private String lname;
    private int age;
    public Person(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }
    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }
    public int getAge() {
        return age;
    }
    @Override
    public String toString() {
        return "Person{fname='" + fname + "', lname='" + lname + "', age=" + age + "}";
    }
    @Override
    public int compareTo(Person p){
        System.out.println("Comparing " + this + " with " + p);
        // if(this.age != p.age){ // For ascending order of age
        //     return this.age - p.age;
        // }
        if(this.age != p.age){ // For descending order of age
            return p.age - this.age;
        }
        //return this.fname.compareTo(p.fname); // For ascending order of first name
        return p.fname.compareTo(this.fname); // For descending order of first name
    }

}
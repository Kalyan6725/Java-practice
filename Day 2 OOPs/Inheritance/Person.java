class Person{
    protected String fname;
    protected String lname;
    protected int age;
    
    public Person(String fname, String lname, int age) {
        this.fname = fname;
        this.lname = lname;
        this.age = age;
    }   
    public void setFname(String fname){
        this.fname=fname;
    }
    public String getFname(){
        return fname;
    }
    public void setLname(String lname){
        this.lname=lname;
    }
    public String getLname(){
        return lname;
    }
    public void setAge(int age){
        this.age=age;
    }
    public int getAge(){
        return age;
    }
    public void eat(){
        System.out.println(fname+" "+lname+" is eating.");
    }
    public void walk(){
        System.out.println(fname+" "+lname+" is walking and age is:"+age);
    }
    public void talk(){
        System.out.println(fname+" "+lname+" is talking.");
    }
    public void displayInfo() {
        System.out.println("Name: " + fname + " " + lname);
        System.out.println("Age: " + age);
    }
}
class Person{
    String fname;
    String lname;
    int age;
    public Person(String fname,String lname,int age){
        this.fname=fname;
        this.lname=lname;
        this.age=age;
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
}
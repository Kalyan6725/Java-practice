class Main3 {
    public static void main(String[] args) {
        Greeting g=new Greeting();
        g.sayHello();
        Person p=new Person("Alice", 30);
        Student s=new Student("Bob", 20, "S12345");
        g.sayHello(null); //calls parent method
        g.sayHello(p); //calls parent method
        // g.sayHello(s); // This would call the Student-specific method if it were uncommented

        Person demoPerson = p.getDemo();
        demoPerson.displayInfo();
    }
}
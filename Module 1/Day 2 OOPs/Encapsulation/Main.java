class Main{

    public static void main(String[] args){
        SuperHero hero1=new SuperHero();
        hero1.setName("Superman");
        hero1.superpower="flight";
        String name=hero1.getName();
        System.out.println(name);
        hero1.useSuperPower();
        hero1.saveTheWorld();

        // SuperHero hero2=new SuperHero();
        // hero2.name="dfgdrf";
        // hero2.superpower="fdfgrd";
        // hero2.useSuperPower();

        SuperVillian v1=new SuperVillian();
        v1.name="Villian";
        v1.superpower="fire";
        v1.useSuperPower();
        v1.destroyTheWorld();

        Person p1=new Person("kalyan","sams",21); //constructor
        // p1.fname="kalyan";
        // p1.lname="sams";
        // p1.age=21;
        p1.eat();
        p1.talk();
        p1.walk();

        Book b1=new Book();
        b1.author="KALS";
        b1.title="GOOD";
        b1.pages=112;
        b1.read();
        b1.getSummary();

        Movie m1=new Movie("Simhadri","SSR",2010,"NTR"); //Constructor
        // m1.setTitle("Simhadri");
        // m1.setYear(2010);
        // m1.setActor("NTR");
        // m1.setDirector("SSR");
        m1.Details();

        
        Employee emp1 = new Employee(101, "Kalyan", 50000);
        emp1.display();

        Flight f1 = new Flight("Delta", "DL101", "New York", "Los Angeles", "2023-10-10 08:00", "2023-10-10 11:00");
        f1.bookFlight();
        f1.showDetails();

        Car car1 = new Car("Toyota", "Camry", 2020, "Red");
        car1.displayInfo();

        Laptop laptop1 = new Laptop("Dell", "XPS 13", "Intel i7", 16, 512);
        laptop1.displayInfo();
        laptop1.turnOn();
        laptop1.turnOff();

        Doctor doc1 = new Doctor("Dr. Smith", "Cardiology", 15);
        doc1.displayDetails();
        doc1.diagnose();
        doc1.treat();

        Teacher t1 = new Teacher("Ms. Johnson", "Mathematics", 10);
        t1.displayDetails();
        t1.teach();

        Artist a1 = new Artist("Leonardo da Vinci", "Renaissance", 500);
        a1.displayDetails();
        a1.perform();

        Athelete ath1 = new Athelete("Usain Bolt", 34, "Sprinting", "Jamaica");
        ath1.displayInfo();
        ath1.compete();
        ath1.train();

        Hotel h1 = new Hotel("Grand Plaza", "New York", 5);
        h1.displayInfo();
        h1.bookRoom();

        Restaurant r1 = new Restaurant("Tasty Bites", "Italian", 4.5);
        r1.displayInfo();
        r1.orderFood();

        Product p11 = new Product("Laptop", 999.99, 1);
        p11.displayInfo();
        p11.applyDiscount(10);
        p11.displayInfo();
    }
    
}
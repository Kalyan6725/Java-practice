class Athelete {
    String name;
    int age;
    String sport;
    String team;

    Athelete(String name, int age, String sport, String team) {
        this.name = name;
        this.age = age;
        this.sport = sport;
        this.team = team;
    }

    void compete() {
        System.out.println(name + " is competing in " + sport);
    }

    void train() {
        System.out.println(name + " is training hard for the next competition.");
    }

    void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Sport: " + sport);
        System.out.println("Team: " + team);
    }
}
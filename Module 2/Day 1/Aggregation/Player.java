class Player {
    public String name;
    public int age;

    Player(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return "Player{name='" + name + "', age=" + age + "}";
    }
}
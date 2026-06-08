class Main3 {
    public static void main(String[] args) {
        Animal animal1 = new Dog();
        Animal animal2 = new Lion();
        Animal animal3 = new Deer();

        animal1.eat();
        animal1.sleep();
        animal2.eat();
        animal2.sleep();
        animal3.eat();
        animal3.sleep();
    }
}
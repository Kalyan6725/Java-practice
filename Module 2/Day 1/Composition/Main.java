class Main{
    public static void main(String[] args) {
        Car car = new Car("Toyota", 150);
        System.out.println("Car model: " + car.getModel());
        System.out.println("Engine horsepower: " + car.getEngine());
    }
}
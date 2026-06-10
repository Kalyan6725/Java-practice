class MainAssociation {
    public static void main(String[] args) {
        Car c=new Car("Tata Nexon");
        Driver d=new Driver("Kalyan");
        d.drive(c);

        Car c2=new Car("Toyota Fortuner");
        d.drive(c2);
    }
}
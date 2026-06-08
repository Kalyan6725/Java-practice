class Car {
    String make;
    String model;
    int year;
    String color;

    public Car(String make, String model, int year, String color) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }
    public void startEngine(){
        System.out.println("The engine of the "+make+" "+model+" is starting.");
    }

    public void stopEngine(){
        System.out.println("The engine of the "+make+" "+model+" is stopping.");
    }

    public void displayInfo(){
        System.out.println("Car Details:");
        System.out.println("Make: "+make);
        System.out.println("Model: "+model);
        System.out.println("Year: "+year);
        System.out.println("Color: "+color);
    }
}
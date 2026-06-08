class Laptop {
    String brand;
    String model;
    String processor;
    int ram;
    int storage;

    public Laptop(String brand, String model, String processor, int ram, int storage) {
        this.brand = brand;
        this.model = model;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
    }

    public void turnOn() {
        System.out.println("The " + brand + " " + model + " is turning on.");
    }

    public void turnOff() {
        System.out.println("The " + brand + " " + model + " is turning off.");
    }

    public void displayInfo() {
        System.out.println("Laptop Details:");
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Processor: " + processor);
        System.out.println("RAM: " + ram + " GB");
        System.out.println("Storage: " + storage + " GB");
    }
}
class Fuel{
    int capacity;
    double currentLevel;
    double mileage;
    public Fuel(int capacity,double currentLevel,double mileage) {
        this.capacity = capacity;
        this.currentLevel = currentLevel;
        this.mileage = mileage;
    }
    void fillFuel(double amount) {
        if (currentLevel + amount <= capacity) {
            currentLevel += amount;
            System.out.println("Fuel filled: " + amount + " liters");
        } else {
            System.out.println("Cannot fill fuel. Exceeds capacity.");
        }
    }

    void checkFuel(){
        System.out.println("Current fuel level: " + currentLevel + " liters");
    }
    
    void drive(double distance) {
        double fuelConsumed = distance / mileage;
        if (fuelConsumed <= currentLevel) {
            currentLevel -= fuelConsumed;
            System.out.println("Driven " + distance + " km. Fuel consumed: " + fuelConsumed + " liters");
        } else {
            System.out.println("Not enough fuel to drive " + distance + " km.");
        }
    }
}
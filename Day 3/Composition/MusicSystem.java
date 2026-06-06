class MusicSystem {
    private String brand;
    private String model;

    public MusicSystem(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public String toString() {
        return "MusicSystem[brand=" + brand + ", model=" + model + "]";
    }
    
}
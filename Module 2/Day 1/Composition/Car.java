class Car {
    private String model;
    private Engine engine;

    public Car(String model,int horsePower) {
        this.model = model;
        this.engine = new Engine(horsePower);
    }

    public Engine getEngine() {
        return engine;
    }
    
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
}
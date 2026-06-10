class Engine {
    private int horsePower;

    public Engine(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public void start() {
        System.out.println("Engine started with " + horsePower + " horsepower.");
    }
    public String toString() {
        return "Engine{horsePower=" + horsePower + "}";
    }
}
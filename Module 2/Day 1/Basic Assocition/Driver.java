class Driver {
    private String name;

    public Driver(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void drive(Car c){
        System.out.println(name + " is driving " + c.getModel());
    }
}
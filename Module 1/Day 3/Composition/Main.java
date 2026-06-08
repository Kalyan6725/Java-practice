class Main{
    public static void main(String[] args) {
        Car car = new Car(new Engine("V8", 400), new Ac(2.5), new MusicSystem("Sony", "X9000H"));
        System.out.println(car);
    }
}
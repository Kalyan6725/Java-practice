class Car {
    private Engine engine;
    private Ac ac;
    private MusicSystem musicSystem;

    public Car(Engine engine, Ac ac, MusicSystem musicSystem) {
        this.engine = engine;
        this.ac = ac;
        this.musicSystem = musicSystem;
    }
    public Engine getEngine() {
        return engine;
    }
    public Ac getAc() {
        return ac;
    }
    public MusicSystem getMusicSystem() {
        return musicSystem;
    }
    public String toString() {
        return "Car[engine=" + engine + ", ac=" + ac + ", musicSystem=" + musicSystem + "]";
    }
}
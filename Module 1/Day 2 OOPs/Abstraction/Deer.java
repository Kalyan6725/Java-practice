class Deer extends Animal{
    @Override
    public void eat() {
        System.out.println("Deer is eating grass");
    }

    @Override
    public void sleep() {
        System.out.println("Deer is sleeping in the forest");
    }

    @Override
    public void talk() {
        System.out.println("Deer is making a sound");
    }
    public void run(){
        System.out.println("Deer is running fast");
    }
}
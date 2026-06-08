class Lion extends Animal {
    @Override
    public void talk() {
        System.out.println("The lion roars.");
    }
    @Override
    public void eat() {
        System.out.println("The lion is eating meat.");
    }
    @Override
    public void sleep() {
        System.out.println("The lion is sleeping in the den.");
    }
}
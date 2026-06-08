class Circle implements Shape {
    private int radius;
    Circle(int radius) {
        this.radius = radius;
    }
    void setRadius(int radius) {
        this.radius = radius;
    }
    int getRadius() {
        return this.radius;
    }
    @Override
    public void draw() {
        System.out.println("Drawing a circle");
    }
    @Override
    public void area() {
        System.out.println("Area of a circle is :"+ (Math.PI * this.radius * this.radius));
    }
}
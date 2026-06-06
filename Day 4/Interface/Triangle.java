class Triangle implements Shape {
    private int base;
    private int height;
    Triangle(int base, int height) {
        this.base = base;
        this.height = height;
    }
    void setBase(int base) {
        this.base = base;
    }
    void setHeight(int height) {
        this.height = height;
    }
    int getBase() {
        return this.base;
    }
    int getHeight() {
        return this.height;
    }
    @Override
    public void draw() {
        System.out.println("Drawing a triangle");
    }

    @Override
    public void area() {
        double area = 0.5 * this.base * this.height;
        System.out.println("Area of the triangle: " + area);
    }
}
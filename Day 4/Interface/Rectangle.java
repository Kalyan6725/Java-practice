class Rectangle implements Shape {
    private int length;
    private int width;

    Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }
    void setLength(int length) {
        this.length = length;
    }
    void setWidth(int width) {
        this.width = width;
    }
    int getLength() {
        return this.length;
    }
    int getWidth() {
        return this.width;
    }
    @Override
    public void draw() {
        System.out.println("Drawing a rectangle");
    }

    @Override
    public void area() {
        int area = this.length * this.width;
        System.out.println("Area of the rectangle: " + area);
    }
}
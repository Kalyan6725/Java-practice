class Main {
    public static void main(String[] args) {
        Circle circle = new Circle(5);
        circle.draw();
        circle.area();

        Rectangle rectangle = new Rectangle(4, 5);
        rectangle.draw();
        rectangle.area();

        Triangle triangle = new Triangle(4, 5);
        triangle.draw();
        triangle.area();

        //Upcasting
        Shape shape1 = new Circle(7);
        Shape.display();
        shape1.draw();
        shape1.area();
    }
}
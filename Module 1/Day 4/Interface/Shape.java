interface Shape {
    String color = "Red"; // public static final by default
    static void display() { // static method in interface
        System.out.println("This is a shape in the color " + color);
    }
    void draw();
    void area();
}
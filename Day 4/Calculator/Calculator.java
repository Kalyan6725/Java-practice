class Calculator {
    public int add(int a, int b) {
        System.out.println("int");
        return a + b;
    }

    public long add(long a, long b) {
        System.out.println("long");
        return a + b;
    }

    public Integer add(Integer a, Integer b) {
        System.out.println("Integer");
        return a + b; // Auto-unboxing: Integer to int, then addition, and auto-boxing back to Integer
    }

    public double add(double a, double b) {
        System.out.println("double");
        return a + b;
    }

    public Double add(Double a, Double b) {
        System.out.println("Double");
        return a + b; // Auto-unboxing: Double to double, then addition, and auto-boxing back to Double
    }

    // public long add(int a, long b) {
    //     System.out.println("int and long");
    //     return a + b; // int is promoted to long, then addition
    // }

    public Long add(Long a, Long b) {
        System.out.println("Long");
        return a + b; // Integer is auto-unboxed to int, then promoted to long, then addition
    }
}
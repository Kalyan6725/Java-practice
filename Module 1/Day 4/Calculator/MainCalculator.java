class MainCalculator {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.add(5, 10)); // Calls add(int, int)
        // System.out.println(calculator.add(Integer.valueOf(5), Integer.valueOf(10))); // Calls add(Integer, Integer)
        // System.out.println(calculator.add(5.12, 10.55)); // Calls add(double, double)
        // System.out.println(calculator.add(Double.valueOf(5.12), Double.valueOf(10.55))); // Calls add(Double, Double)
        // System.out.println(calculator.add(Long.valueOf(100L),Long.valueOf(100L)));
    }
}
class MainDouble {
    public static void main(String[] args) {
        double a = 3.14;
        //Double doubleObj = new Double(a); // Boxing: converting primitive double to Double object
        Double doubleObj = Double.valueOf(a); // Boxing: converting primitive double to Double object
        System.out.println("Value of doubleObj: " + doubleObj);

        // Auto-boxing: Java automatically converts primitive double to Double object
        Double autoBoxedDouble = a;
        System.out.println("Value of autoBoxedDouble: " + autoBoxedDouble);

        // Unboxing: converting Double object back to primitive double
        double unboxedDouble = doubleObj.doubleValue();
        System.out.println("Value of unboxedDouble: " + unboxedDouble);
    }
}
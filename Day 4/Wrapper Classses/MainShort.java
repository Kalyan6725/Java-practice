class MainShort {
    public static void main(String[] args) {
        short a = 100;
        //Short shortObj = new Short(a); // Boxing: converting primitive short to Short object
        Short shortObj = Short.valueOf(a); // Boxing: converting primitive short to Short object
        System.out.println("Value of shortObj: " + shortObj);

        Short autoBoxedShort = a; // Auto-boxing: Java automatically converts primitive short to Short object
        System.out.println("Value of autoBoxedShort: " + autoBoxedShort);

        // Unboxing: converting Short object back to primitive short
        short unboxedShort = autoBoxedShort.shortValue();
        System.out.println("Value of unboxedShort: " + unboxedShort);
    }
}
class MainLong {
    public static void main(String[] args) {
        long a = 10000000000L;
        //Long longObj = new Long(a); // Boxing: converting primitive long to Long object
        Long longObj = Long.valueOf(a); // Boxing: converting primitive long to Long object
        System.out.println("Value of longObj: " + longObj);
        
        Long autoBoxedLong = a; // Auto-boxing: Java automatically converts primitive long to Long object
        System.out.println("Value of autoBoxedLong: " + autoBoxedLong);

        // Unboxing: converting Long object back to primitive long
        long unboxedLong = autoBoxedLong.longValue();
        System.out.println("Value of unboxedLong: " + unboxedLong);
    }
}
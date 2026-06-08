class Main{
    public static void main(String[] args) {

        //Wrapper Classes

        int a = 10;
        Integer integer=new Integer(a); // Boxing: converting primitive int to Integer object
        System.out.println("Value of integer: " + integer);

        //Auto-boxing: Java automatically converts primitive int to Integer object
        Integer autoBoxedInteger = a;
        System.out.println("Value of autoBoxedInteger: " + autoBoxedInteger);

        // Unboxing: converting Integer object back to primitive int
        int unboxedInt = integer.intValue();
        System.out.println("Value of unboxedInt: " + unboxedInt);
    }
}
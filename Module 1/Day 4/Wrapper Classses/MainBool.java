class MainBool {
    public static void main(String[] args) {
        boolean isJavaFun = true;
        //Boolean boolObj = new Boolean(isJavaFun); // Boxing: converting primitive boolean to Boolean object
        Boolean boolObj = Boolean.valueOf(isJavaFun); // Boxing: converting primitive boolean to

        //Auto-boxing: Java automatically converts primitive boolean to Boolean object
        Boolean autoBoxedBool = isJavaFun;
        System.out.println("Value of autoBoxedBool: " + autoBoxedBool);

        // Unboxing: converting Boolean object back to primitive boolean
        boolean unboxedBool = boolObj.booleanValue();
        System.out.println("Value of unboxedBool: " + unboxedBool);
    }
}
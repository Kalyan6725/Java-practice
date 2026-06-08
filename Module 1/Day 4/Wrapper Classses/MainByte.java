class MainByte {
    public static void main(String[] args) {
        byte a = 10;
        
        Byte byteObj = Byte.valueOf(a); // Boxing: converting primitive byte to Byte object
        System.out.println("Value of byteObj: " + byteObj);

        Byte autoBoxedByte = a; // Auto-boxing: Java automatically converts primitive byte to Byte object
        System.out.println("Value of autoBoxedByte: " + autoBoxedByte);

        // Unboxing: converting Byte object back to primitive byte
        byte unboxedByte = autoBoxedByte.byteValue();
        System.out.println("Value of unboxedByte: " + unboxedByte);
    }
}
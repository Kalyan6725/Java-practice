class Main{
    public static void main(String[] args) {
        Overloading overloading = new Overloading();
        
        System.out.println("Sum of two integers: " + overloading.add(5, 10));
        
        // int sum2 = overloading.add(5, 10, 15);
        // System.out.println("Sum of three integers: " + sum2);
        
        // double sum3 = overloading.add(5.512, 10.554);
        // System.out.println("Sum of two doubles: " + sum3);
        
        // byte sum4 = overloading.add((byte) 5, (byte) 10);
        // System.out.println("Sum of two bytes: " + sum4);
        
        // float sum5 = overloading.add(5.5f, 10.5f);
        // System.out.println("Sum of two floats: " + sum5);

        // System.out.println("Sum of two longs: " + overloading.add(100L, 20L));
    }
}
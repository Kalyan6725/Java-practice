class Overloading {
    // public int add(int a, int b) {
    //     System.out.println("This is integer addition");
    //     return a + b;
    // }

    public int add(int a, int b, int c) {  
        System.out.println("This is three-number addition");
        return a + b + c;
    }

    public double add(double a, double b) {
        System.out.println("This is double addition");
        return a + b;
    }

    public long add(long a, long b) {
        System.out.println("This is long addition");
        return a + b;
    }

    public float add(float a, float b) {
        System.out.println("This is float addition");
        return a + b;
    }

    public byte add(byte a, byte b) {
        System.out.println("This is byte addition");
        return (byte) (a + b);
    }


}
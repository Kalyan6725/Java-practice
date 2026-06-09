class MainOverloading {
    public static void main(String[] args) {
        //Overloading
        FeeCalculator f = new FeeCalculator();
        System.out.println(f.calculateFee(1000));
        System.out.println(f.calculateFee(1000, true));
    }
}
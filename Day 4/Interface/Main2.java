class Main2 {
    public static void main(String[] args) {
        Payment payment1 = new UPI();
        Payment payment2 = new Debit();
        Payment payment3 = new Credit();

        payment1.processPayment(100.0);
        payment2.processPayment(200.0);
        payment3.processPayment(300.0);

        //UPI.display(); 

        Payment.display(); // Calling static method in interface
    }
}
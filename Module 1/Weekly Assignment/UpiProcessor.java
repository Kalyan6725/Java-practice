class UpiProcessor extends PaymentProcessor {

    @Override
    void processPayment(double amount) {
        System.out.println("UPI Payment: " + amount);
    }
}
abstract class PaymentProcessor {

    abstract void processPayment(double amount);

    void validate() {
        System.out.println("KYC Validation");
    }
}
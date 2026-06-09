class MainAbstraction {
    public static void main(String[] args) {
        PaymentProcessor p = new UpiProcessor();
        p.validate();
        p.processPayment(5000);
    }
}


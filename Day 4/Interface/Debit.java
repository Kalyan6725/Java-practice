class Debit implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing debit card payment of $" + amount);
    }
}
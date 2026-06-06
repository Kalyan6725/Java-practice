interface Payment {
    static void display() {
        System.out.println("This is a payment interface");
    }
    void processPayment(double amount);
}
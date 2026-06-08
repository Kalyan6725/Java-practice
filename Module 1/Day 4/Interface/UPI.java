class UPI implements Payment {
    @Override
    public void processPayment(double amount) { //what is use of public here? Its overriding public to package public, which is not allowed in Java. So we need to make it public.
        System.out.println("Processing UPI payment of $" + amount);
    }
}
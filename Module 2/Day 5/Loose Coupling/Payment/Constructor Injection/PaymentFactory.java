class PaymentFactory {
    private static CreditCard creditCard= new CreditCard();
    private static DebitCard debitCard = new DebitCard();
    private static UPI upi = new UPI();
    public static PaymentService getPaymentService(String type) {
        if (type.equalsIgnoreCase("credit")) {
            return creditCard;
        } else if (type.equalsIgnoreCase("debit")) {
            return debitCard;
        } else if (type.equalsIgnoreCase("upi")) {
            return upi;
        }
        return null;
    }
}
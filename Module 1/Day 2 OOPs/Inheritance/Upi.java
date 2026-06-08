class Upi extends Payments {
    String upiId;

    Upi(String upiId) {
        this.upiId = upiId;
    }

    void displayUpiDetails() {
        System.out.println("UPI ID: " + upiId);
    }

    void pay() {
        super.pay();  // Call the parent class's pay method
        System.out.println("Payment done using UPI with ID: " + upiId);
    }
}
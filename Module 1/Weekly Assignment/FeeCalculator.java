class FeeCalculator {

    double calculateFee(double amount) {
        return amount * 0.01;
    }

    double calculateFee(double amount, boolean priority) {
        return priority ? amount * 0.02 : amount * 0.01;
    }
}
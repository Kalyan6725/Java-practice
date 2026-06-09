class MainEncapsulation {
    public static void main(String[] args) {
        CreditBureauReport report = new CreditBureauReport("12345", 750);
        System.out.println("Initial Credit Score: " + report.getCreditScore());
        report.setCreditScore(800);
        System.out.println("Updated Credit Score: " + report.getCreditScore());
    }
}
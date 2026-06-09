class CreditBureauReport {
    private String customerId;
    private int creditScore;

    public CreditBureauReport(String customerId, int creditScore) {
        this.customerId = customerId;
        this.creditScore = creditScore;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        if (creditScore >= 300 && creditScore <= 900)
            this.creditScore = creditScore;
    }
}
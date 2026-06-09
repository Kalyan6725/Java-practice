class RiskAssessment {
    void calculateRisk() {
        System.out.println("Generic Risk Calculation");
    }
}

class MSMERisk extends RiskAssessment {
    @Override
    void calculateRisk() {
        System.out.println("MSME Risk Model");
    }
}

class ConsumerRisk extends RiskAssessment {
    @Override
    void calculateRisk() {
        System.out.println("Consumer Risk Model");
    }
}
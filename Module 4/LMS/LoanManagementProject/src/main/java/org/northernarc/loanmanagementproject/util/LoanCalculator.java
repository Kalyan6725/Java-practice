package org.northernarc.loanmanagementproject.util;

/**
 * LoanCalculator - Pure financial helper for EMI and amortization maths.
 * Kept free of Spring/JPA so it can be unit-tested in isolation.
 */
public final class LoanCalculator {

    private LoanCalculator() {
    }

    /**
     * Compute the monthly EMI using the standard reducing-balance formula:
     * EMI = P * r * (1+r)^n / ((1+r)^n - 1)
     * where r = annualInterestRate / 12 / 100 and n = tenure in months.
     * When the interest rate is zero the EMI is simply principal / tenure.
     *
     * @param principal          loan principal
     * @param annualInterestRate annual interest rate as a percentage (e.g. 12.5)
     * @param tenureMonths       number of monthly installments (> 0)
     * @return the monthly EMI rounded to 2 decimals
     */
    public static double calculateEmi(double principal, double annualInterestRate, int tenureMonths) {
        if (tenureMonths <= 0) {
            throw new IllegalArgumentException("Tenure must be greater than zero");
        }
        if (annualInterestRate <= 0.0) {
            return round2(principal / tenureMonths);
        }
        double r = annualInterestRate / 12.0 / 100.0;
        double pow = Math.pow(1 + r, tenureMonths);
        double emi = principal * r * pow / (pow - 1);
        return round2(emi);
    }

    /**
     * The monthly interest portion for a given outstanding balance.
     */
    public static double monthlyInterest(double outstandingBalance, double annualInterestRate) {
        if (annualInterestRate <= 0.0) {
            return 0.0;
        }
        double r = annualInterestRate / 12.0 / 100.0;
        return round2(outstandingBalance * r);
    }

    /**
     * Penalty for a late payment: penalty = emiAmount * dailyPenaltyRate% * daysLate.
     */
    public static double penalty(double emiAmount, double dailyPenaltyRate, long daysLate) {
        if (daysLate <= 0 || dailyPenaltyRate <= 0.0) {
            return 0.0;
        }
        return round2(emiAmount * (dailyPenaltyRate / 100.0) * daysLate);
    }

    public static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

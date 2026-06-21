package org.northernarc.weeklyassignment.service;

import org.northernarc.weeklyassignment.entity.LoanApplication;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface LendingAnalyticsService {

    LoanApplication addApplication(LoanApplication application);
    List<LoanApplication> getAllApplications();
    Optional<LoanApplication> getApplicationById(String id);
    LoanApplication updateApplication(String id, LoanApplication application);
    boolean deleteApplication(String id);
    void loadApplications(List<String> records);
    List<LoanApplication> topCreditProfiles(int n);
    Map<String, Double> averageLoanAmountByType();
    Optional<LoanApplication> highestLoanApplication();
    Set<String> lendersWithMultipleLoanTypes();
    Map<String,List<LoanApplication>> groupApplicationsByLender();
    List<String> suspiciousApplications();
    Map<String, Map<String,Optional<LoanApplication>>>loanTypeWiseTopApplicantByLender();
}
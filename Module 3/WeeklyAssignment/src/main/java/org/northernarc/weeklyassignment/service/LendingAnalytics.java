package org.northernarc.weeklyassignment.service;

import org.northernarc.weeklyassignment.entity.LoanApplication;
import org.northernarc.weeklyassignment.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LendingAnalytics implements LendingAnalyticsService {
    @Autowired
    private LoanApplicationRepository repository;
    @Override
    public LoanApplication addApplication(LoanApplication application) {
        Optional<LoanApplication> existing = repository.findById(application.getApplicationId());
        if (existing.isEmpty()) {  //if the id does not exist, add the application
            return repository.save(application);
        }
        LoanApplication old = existing.get(); // otherwise deal with the priority
        if (application.getCreditScore()> old.getCreditScore()) {//higher credit score
            return repository.save(application);
        }
        if (application.getCreditScore() == old.getCreditScore()) {
            if (application.getLoanAmount() < old.getLoanAmount()) {//lower loan amount
                return repository.save(application);
            }
            if (application.getLoanAmount() == old.getLoanAmount()) {
                if (application.getCustomerName().compareToIgnoreCase(old.getCustomerName()) < 0) { //lexicographically small name

                    return repository.save(application);
                }
            }
        }

        return old;
    }

    @Override
    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }

    @Override
    public Optional<LoanApplication> getApplicationById(String applicationId) {
        return repository.findById(applicationId);
    }

    @Override
    public LoanApplication updateApplication(String applicationId, LoanApplication application) {
        Optional<LoanApplication> existing = repository.findById(applicationId);
        if (existing.isPresent()) {
            LoanApplication old = existing.get();
            old.setCustomerName(application.getCustomerName());
            old.setLenderName(application.getLenderName());
            old.setLoanType(application.getLoanType());
            old.setLoanAmount(application.getLoanAmount());
            old.setCreditScore(application.getCreditScore());
            return repository.save(old);
        }
        return null;
    }

    @Override
    public boolean deleteApplication(String applicationId) {
        Optional<LoanApplication> existing =
                repository.findById(applicationId);
        if (existing.isPresent()) {
            repository.deleteById(applicationId);
            System.out.println("Deleted application with ID: " + applicationId);
            return true;
        }
        System.out.println("Application with ID: " + applicationId + " not found for deletion.");
        return false;
    }

    @Override
    public void loadApplications(List<String> records) {
        if (records == null) {
            return;
        }
        records.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank())
                .forEach(record -> {
                    String[] parts = record.split("\\|");
                    if (parts.length != 6) {
                        return;
                    }
                    String id = parts[0].trim();
                    String customerName = parts[1].trim();
                    String lenderName = parts[2].trim();
                    String loanType = parts[3].trim();
                    double loanAmount;
                    int creditScore;
                    try {
                        loanAmount = Double.parseDouble(parts[4].trim());
                        creditScore = Integer.parseInt(parts[5].trim());
                    } catch (Exception e) {
                        return;
                    }
                    if (id.isEmpty()
                            || customerName.isEmpty() || lenderName.isEmpty() || loanType.isEmpty() || loanAmount <= 0
                            || creditScore < 300 || creditScore > 900) {
                        return;
                    }
                    LoanApplication application =
                            new LoanApplication(id,customerName,lenderName,loanType,loanAmount,creditScore);
                    addApplication(application);
                });
    }

    @Override
    public List<LoanApplication> topCreditProfiles(int n) {

        if (n <= 0) {
            return new ArrayList<>();
        }

        return repository.findAll()
                .stream()
                .sorted(
                        Comparator
                                .comparingInt(
                                        LoanApplication::getCreditScore)
                                .reversed()
                                .thenComparing(
                                        LoanApplication::getLoanAmount)
                                .thenComparing(
                                        LoanApplication::getCustomerName,
                                        String.CASE_INSENSITIVE_ORDER)
                ).limit(n).toList();
    }

    @Override
    public Map<String, Double> averageLoanAmountByType() {

        return repository.findAll()
                .stream().collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(LoanApplication::getLoanAmount),
                                avg -> Math.round(avg * 100.0) / 100.0)
                ));
    }


    @Override
    public Optional<LoanApplication> highestLoanApplication() {

        return repository.findAll().stream().max(
                Comparator.comparingDouble(LoanApplication::getLoanAmount)
                        .thenComparing(LoanApplication::getCreditScore)
                        .thenComparing(LoanApplication::getApplicationId,Comparator.reverseOrder()));
    }


    @Override
    public Set<String> lendersWithMultipleLoanTypes() {

        return repository.findAll().stream().collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        Collectors.mapping(LoanApplication::getLoanType, Collectors.toSet())
                )).entrySet().stream().filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Map<String, List<LoanApplication>> groupApplicationsByLender() {

        return repository.findAll().stream()
                .sorted(Comparator.comparing(LoanApplication::getLenderName))
                .collect(Collectors.groupingBy(LoanApplication::getLenderName,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(
                                                        LoanApplication::getCreditScore).reversed()
                                                .thenComparing(LoanApplication::getLoanAmount)).toList()
                        )
                ));
    }


    @Override
    public List<String> suspiciousApplications() {

        List<LoanApplication> applications = repository.findAll();

        Map<String, Double> averageAmount =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingDouble(
                                        LoanApplication::getLoanAmount)
                        ));

        Map<String, Double> averageCreditScore =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                LoanApplication::getLoanType,
                                Collectors.averagingInt(
                                        LoanApplication::getCreditScore)
                        ));

        // Condition 6
        Map<String, Set<String>> customerLenders =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                application ->
                                        application.getCustomerName().toLowerCase(),
                                Collectors.mapping(
                                        LoanApplication::getLenderName,
                                        Collectors.toSet()
                                )
                        ));

        // Condition 7
        Map<String, Set<String>> sameTypeAmountScore =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                application ->
                                        application.getLoanType() + "|"
                                                + application.getLoanAmount() + "|"
                                                + application.getCreditScore(),
                                Collectors.mapping(
                                        application ->
                                                application.getCustomerName().toLowerCase(),
                                        Collectors.toSet()
                                )
                        ));

        // Condition 8
        Map<String, Set<String>> lenderAnagrams =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                application -> {

                                    char[] chars =
                                            application.getCustomerName()
                                                    .toLowerCase()
                                                    .replaceAll("\\s+", "")
                                                    .toCharArray();

                                    Arrays.sort(chars);

                                    return application.getLenderName()
                                            + "|"
                                            + new String(chars);
                                },
                                Collectors.mapping(
                                        application ->
                                                application.getCustomerName().toLowerCase(),
                                        Collectors.toSet()
                                )
                        ));

        Set<String> suspicious = new TreeSet<>();

        for (LoanApplication application : applications) {
            boolean flag = false;
            String customerName = application.getCustomerName();
            String lowerName =  customerName.toLowerCase();
            // Condition 1
            // Consecutive repeated words

            if (!flag) {
                String[] words = lowerName.trim().split("\\s+");
                for (int i = 0; i < words.length - 1; i++) {
                    if (words[i].equals(words[i + 1])) {
                        flag = true;
                        break;
                    }
                }
            }

            // Condition 2
            // Lender name present in customer name
            if (!flag) {
                String lenderName = application.getLenderName().toLowerCase();
                if (lowerName.contains(lenderName)) {
                    flag = true;
                }
            }

            // Condition 3
            // Amount > 250% of average amount
            if (!flag) {
                double avgAmount = averageAmount.get(application.getLoanType());
                if (avgAmount != 0 && application.getLoanAmount() > 2.5 * avgAmount) {
                    flag = true;
                }
            }
            // Condition 4
            // Credit score below average and amount above average
            if (!flag) {
                double avgCredit =averageCreditScore.get(application.getLoanType());
                double avgAmount =averageAmount.get(application.getLoanType());
                if (application.getCreditScore() < avgCredit && application.getLoanAmount() > avgAmount) {
                    flag = true;
                }
            }
            // Condition 5
            // More than 3 words
            if (!flag) {
                String[] words = customerName.trim().split("\\s+");
                if (words.length > 3) {
                    flag = true;
                }
            }

            // Condition 6
            // Same customer with more than 3 lenders
            if (!flag) {
                Set<String> lenders = customerLenders.get(lowerName);
                if (lenders.size() > 3) {
                    flag = true;
                }
            }
            // Condition 7
            // Same type, amount and credit score with different names
            if (!flag) {
                String key =
                        application.getLoanType() + "|" + application.getLoanAmount() + "|" + application.getCreditScore();
                Set<String> names = sameTypeAmountScore.get(key);
                if (names.size() > 1) {
                    flag = true;
                }
            }
            // Condition 8
            // Anagram names under same lender
            if (!flag) {
                char[] chars = customerName.toLowerCase().replaceAll("\\s+", "").toCharArray();
                Arrays.sort(chars);
                String key = application.getLenderName() + "|" + new String(chars);
                Set<String> names = lenderAnagrams.get(key);
                if (names.size() > 1) {
                    flag = true;
                }
            }
            if (flag) {
                suspicious.add(customerName);
            }
        }
        return new ArrayList<>(suspicious);
    }


    @Override
    public Map<String,Map<String, Optional<LoanApplication>>>loanTypeWiseTopApplicantByLender() {

        return repository.findAll().stream().collect(Collectors.groupingBy(
                LoanApplication::getLoanType,
                Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        Collectors.maxBy(
                                Comparator.comparingInt(LoanApplication::getCreditScore)
                                        .thenComparing(LoanApplication::getLoanAmount,Comparator.reverseOrder())
                        )
                )
        ));
    }

}

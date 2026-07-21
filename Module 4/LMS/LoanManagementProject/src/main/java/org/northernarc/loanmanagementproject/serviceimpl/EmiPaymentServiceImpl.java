package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.dto.request.EmiPaymentRequest;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentResponse;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.exception.LoanAccountNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.northernarc.loanmanagementproject.repository.EmiPaymentRepository;
import org.northernarc.loanmanagementproject.repository.LoanAccountRepository;
import org.northernarc.loanmanagementproject.service.EmiPaymentService;
import org.northernarc.loanmanagementproject.util.LoanCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmiPaymentServiceImpl implements EmiPaymentService {

    private final EmiPaymentRepository emiPaymentRepository;
    private final LoanAccountRepository loanAccountRepository;

    public EmiPaymentServiceImpl(EmiPaymentRepository emiPaymentRepository,
                                 LoanAccountRepository loanAccountRepository) {
        this.emiPaymentRepository = emiPaymentRepository;
        this.loanAccountRepository = loanAccountRepository;
    }

    @Override
    public EmiPayment pay(EmiPaymentRequest request) {
        LoanAccount account = loanAccountRepository.findById(request.getLoanAccountId())
                .orElseThrow(() -> new LoanAccountNotFoundException(request.getLoanAccountId().toString()));

        if ("CLOSED".equals(account.getStatus())) {
            throw new ValidationException("Loan account is already closed");
        }
        if ("APPROVED".equals(account.getStatus())) {
            throw new ValidationException("Loan must be disbursed before EMI payments can be made");
        }

        int tenure = account.getTenureMonths();
        List<EmiPayment> existing = emiPaymentRepository.findByLoanAccountId(account.getLoanAccountId());
        int paidCount = existing.size();

        int installmentNo = request.getInstallmentNo() != null ? request.getInstallmentNo() : paidCount + 1;
        if (installmentNo < 1 || installmentNo > tenure) {
            throw new ValidationException("Invalid installment number " + installmentNo
                    + " for a tenure of " + tenure + " months");
        }
        boolean alreadyPaid = existing.stream()
                .anyMatch(p -> p.getInstallmentNo() != null && p.getInstallmentNo() == installmentNo);
        if (alreadyPaid) {
            throw new ValidationException("Installment " + installmentNo + " has already been paid");
        }

        LocalDate startDate = account.getLoanStartDate();
        LocalDate dueDate = startDate.plusMonths(installmentNo);

        double rate = account.getInterestRate() != null ? account.getInterestRate() : 0.0;
        double scheduledEmi = account.getEmiAmount() != null ? account.getEmiAmount() : 0.0;

        double principalPaidSoFar = existing.stream()
                .filter(p -> p.getPrincipalPaid() != null)
                .mapToDouble(EmiPayment::getPrincipalPaid)
                .sum();
        double outstanding = account.getLoanAmount() - principalPaidSoFar;

        double interest = LoanCalculator.monthlyInterest(outstanding, rate);
        double principal = LoanCalculator.round2(scheduledEmi - interest);
        // Final installment clears any rounding remainder.
        if (installmentNo == tenure || principal > outstanding) {
            principal = LoanCalculator.round2(outstanding);
        }

        double dailyPenaltyRate = account.getLoanProduct() != null && account.getLoanProduct().getDailyPenaltyRate() != null
                ? account.getLoanProduct().getDailyPenaltyRate() : 0.0;
        LocalDate today = LocalDate.now();
        long daysLate = today.isAfter(dueDate) ? ChronoUnit.DAYS.between(dueDate, today) : 0;
        double penalty = LoanCalculator.penalty(scheduledEmi, dailyPenaltyRate, daysLate);

        double total = request.getAmount() != null
                ? request.getAmount()
                : LoanCalculator.round2(principal + interest + penalty);

        EmiPayment payment = new EmiPayment();
        payment.setLoanAccount(account);
        payment.setInstallmentNo(installmentNo);
        payment.setDueDate(dueDate);
        payment.setPaymentDate(today);
        payment.setEmiAmount(scheduledEmi);
        payment.setPrincipalPaid(principal);
        payment.setInterestPaid(interest);
        payment.setPenaltyPaid(penalty);
        payment.setAmountPaid(total);
        payment.setPaymentType(request.getPaymentType());
        payment.setStatus("PAID");
        EmiPayment saved = emiPaymentRepository.save(payment);

        int newPaidCount = paidCount + 1;
        if (newPaidCount >= tenure) {
            account.setStatus("CLOSED");
            account.setLoanCloseDate(today);
            account.setEmiDueDate(null);
        } else {
            account.setStatus("ACTIVE");
            account.setEmiDueDate(startDate.plusMonths(newPaidCount + 1));
        }
        loanAccountRepository.save(account);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public EmiPayment getById(Long paymentId) {
        return emiPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new ValidationException("EMI payment not found with ID: " + paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmiPayment> getAll() {
        return emiPaymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmiPayment> getByLoanAccountId(Long loanAccountId) {
        return emiPaymentRepository.findByLoanAccountId(loanAccountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmiPayment> getByPaymentType(String paymentType) {
        return emiPaymentRepository.findByPaymentType(paymentType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmiPaymentResponse> getSchedule(Long loanAccountId) {
        LoanAccount account = loanAccountRepository.findById(loanAccountId)
                .orElseThrow(() -> new LoanAccountNotFoundException(loanAccountId.toString()));

        int tenure = account.getTenureMonths();
        double rate = account.getInterestRate() != null ? account.getInterestRate() : 0.0;
        double scheduledEmi = account.getEmiAmount() != null ? account.getEmiAmount() : 0.0;
        LocalDate startDate = account.getLoanStartDate();

        Map<Integer, EmiPayment> paidByInstallment = emiPaymentRepository
                .findByLoanAccountId(loanAccountId).stream()
                .filter(p -> p.getInstallmentNo() != null)
                .collect(Collectors.toMap(EmiPayment::getInstallmentNo, Function.identity(), (a, b) -> a));

        List<EmiPaymentResponse> schedule = new ArrayList<>();
        double balance = account.getLoanAmount();

        for (int i = 1; i <= tenure; i++) {
            double interest = LoanCalculator.monthlyInterest(balance, rate);
            double principal = LoanCalculator.round2(scheduledEmi - interest);
            if (i == tenure || principal > balance) {
                principal = LoanCalculator.round2(balance);
            }
            LocalDate dueDate = startDate != null ? startDate.plusMonths(i) : null;

            EmiPayment actual = paidByInstallment.get(i);
            EmiPaymentResponse.EmiPaymentResponseBuilder builder = EmiPaymentResponse.builder()
                    .loanAccountId(account.getLoanAccountId())
                    .loanNumber(account.getLoanNumber())
                    .installmentNo(i)
                    .dueDate(dueDate);

            if (actual != null) {
                builder.paymentId(actual.getPaymentId())
                        .paymentDate(actual.getPaymentDate())
                        .emiAmount(actual.getEmiAmount())
                        .principalPaid(actual.getPrincipalPaid())
                        .interestPaid(actual.getInterestPaid())
                        .penaltyPaid(actual.getPenaltyPaid())
                        .totalPaid(actual.getAmountPaid())
                        .paymentType(actual.getPaymentType())
                        .status("PAID");
            } else {
                boolean overdue = dueDate != null && LocalDate.now().isAfter(dueDate);
                builder.emiAmount(LoanCalculator.round2(principal + interest))
                        .principalPaid(principal)
                        .interestPaid(interest)
                        .penaltyPaid(0.0)
                        .totalPaid(0.0)
                        .status(overdue ? "OVERDUE" : "PENDING");
            }

            schedule.add(builder.build());
            balance = LoanCalculator.round2(balance - principal);
        }
        return schedule;
    }
}

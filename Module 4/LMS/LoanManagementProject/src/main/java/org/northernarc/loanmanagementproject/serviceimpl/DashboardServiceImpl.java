package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.repository.EmiPaymentRepository;
import org.northernarc.loanmanagementproject.repository.LoanAccountRepository;
import org.northernarc.loanmanagementproject.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final LoanAccountRepository loanAccountRepository;
    private final EmiPaymentRepository emiPaymentRepository;

    public DashboardServiceImpl(CustomerRepository customerRepository,
                                LoanAccountRepository loanAccountRepository,
                                EmiPaymentRepository emiPaymentRepository) {
        this.customerRepository = customerRepository;
        this.loanAccountRepository = loanAccountRepository;
        this.emiPaymentRepository = emiPaymentRepository;
    }

    @Override
    public DashboardDTO getDashboardData() {
        Long totalCustomers = customerRepository.getTotalCustomerCount();
        Long totalLoans = loanAccountRepository.getTotalLoanCount();
        Double totalLoanAmount = loanAccountRepository.getTotalLoanAmountDisbursed();
        Double totalPenalty = emiPaymentRepository.getTotalPenaltyCollected();
        String topBranch = customerRepository.getTopBranch();
        String highestLoanCustomer = customerRepository.getHighestLoanCustomer();

        return DashboardDTO.builder()
                .totalCustomers(totalCustomers != null ? totalCustomers : 0L)
                .totalLoans(totalLoans != null ? totalLoans : 0L)
                .totalLoanAmountDisbursed(totalLoanAmount != null ? totalLoanAmount : 0.0)
                .totalPenaltyCollected(totalPenalty != null ? totalPenalty : 0.0)
                .topBranch(topBranch != null ? topBranch : "N/A")
                .highestLoanCustomer(highestLoanCustomer != null ? highestLoanCustomer : "N/A")
                .build();
    }
}

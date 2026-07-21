package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.dto.request.LoanApplicationRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationReviewRequest;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.LoanApplication;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanApplicationNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanProductNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.repository.LoanApplicationRepository;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.service.LoanAccountService;
import org.northernarc.loanmanagementproject.service.LoanApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;
    private final LoanProductRepository loanProductRepository;
    private final LoanAccountService loanAccountService;

    public LoanApplicationServiceImpl(LoanApplicationRepository loanApplicationRepository,
                                      CustomerRepository customerRepository,
                                      LoanProductRepository loanProductRepository,
                                      LoanAccountService loanAccountService) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.customerRepository = customerRepository;
        this.loanProductRepository = loanProductRepository;
        this.loanAccountService = loanAccountService;
    }

    @Override
    public LoanApplication apply(LoanApplicationRequest request, Long applicantCustomerId) {
        Long customerId = request.getCustomerId() != null ? request.getCustomerId() : applicantCustomerId;
        if (customerId == null) {
            throw new ValidationException("Customer could not be determined for this application");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new ValidationException("Customer account is not ACTIVE and cannot apply for loans");
        }

        LoanProduct product = loanProductRepository.findById(request.getLoanCode())
                .orElseThrow(() -> new LoanProductNotFoundException(request.getLoanCode()));
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ValidationException("Loan product " + product.getLoanCode() + " is not available");
        }

        double amount = request.getRequestedAmount();
        if (product.getMinimumAmount() != null && amount < product.getMinimumAmount()) {
            throw new ValidationException("Requested amount is below the product minimum of " + product.getMinimumAmount());
        }
        if (product.getMaximumAmount() != null && amount > product.getMaximumAmount()) {
            throw new ValidationException("Requested amount exceeds the product maximum of " + product.getMaximumAmount());
        }

        int tenure = request.getTenureMonths();
        if (product.getMinimumTenure() != null && tenure < product.getMinimumTenure()) {
            throw new ValidationException("Tenure is below the product minimum of " + product.getMinimumTenure() + " months");
        }
        if (product.getMaximumTenure() != null && tenure > product.getMaximumTenure()) {
            throw new ValidationException("Tenure exceeds the product maximum of " + product.getMaximumTenure() + " months");
        }

        if (loanApplicationRepository.existsOpenApplication(customerId, product.getLoanCode())) {
            throw new ValidationException("An open application already exists for this product");
        }

        LoanApplication application = new LoanApplication();
        application.setCustomer(customer);
        application.setLoanProduct(product);
        application.setRequestedAmount(amount);
        application.setTenureMonths(tenure);
        application.setApplicationDate(LocalDate.now());
        application.setStatus("SUBMITTED");
        return loanApplicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplication getById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new LoanApplicationNotFoundException(applicationId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> getByCustomer(Long customerId) {
        return loanApplicationRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> getAll() {
        return loanApplicationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> getByStatus(String status) {
        return loanApplicationRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplication> getOpenQueue() {
        return loanApplicationRepository.findOpenApplications();
    }

    @Override
    public LoanApplication review(Long applicationId, LoanApplicationReviewRequest request) {
        LoanApplication application = getById(applicationId);

        if ("APPROVED".equals(application.getStatus()) || "REJECTED".equals(application.getStatus())) {
            throw new ValidationException("Application has already been finalised with status: " + application.getStatus());
        }

        String decision = request.getDecision();
        application.setRemarks(request.getRemarks());

        switch (decision) {
            case "UNDER_REVIEW" -> application.setStatus("UNDER_REVIEW");
            case "REJECTED" -> application.setStatus("REJECTED");
            case "APPROVED" -> {
                application.setStatus("APPROVED");
                Double approvedAmount = request.getApprovedAmount() != null
                        ? request.getApprovedAmount() : application.getRequestedAmount();
                loanAccountService.createFromApplication(application, approvedAmount);
            }
            default -> throw new ValidationException("Unsupported decision: " + decision);
        }

        return loanApplicationRepository.save(application);
    }
}

package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.dto.request.LoanProductRequest;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.exception.LoanProductNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.service.LoanProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LoanProductServiceImpl implements LoanProductService {

    private final LoanProductRepository loanProductRepository;

    public LoanProductServiceImpl(LoanProductRepository loanProductRepository) {
        this.loanProductRepository = loanProductRepository;
    }

    @Override
    public LoanProduct create(LoanProductRequest request) {
        if (loanProductRepository.existsById(request.getLoanCode())) {
            throw new ValidationException("Loan product already exists with code: " + request.getLoanCode());
        }
        validateRanges(request);
        LoanProduct product = new LoanProduct();
        apply(product, request);
        return loanProductRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanProduct getByCode(String loanCode) {
        return loanProductRepository.findById(loanCode)
                .orElseThrow(() -> new LoanProductNotFoundException(loanCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanProduct> getAll() {
        return loanProductRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanProduct> getAllActive() {
        return loanProductRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActive()))
                .toList();
    }

    @Override
    public LoanProduct update(LoanProductRequest request) {
        LoanProduct existing = loanProductRepository.findById(request.getLoanCode())
                .orElseThrow(() -> new LoanProductNotFoundException(request.getLoanCode()));
        validateRanges(request);
        apply(existing, request);
        return loanProductRepository.save(existing);
    }

    @Override
    public void delete(String loanCode) {
        LoanProduct existing = loanProductRepository.findById(loanCode)
                .orElseThrow(() -> new LoanProductNotFoundException(loanCode));
        loanProductRepository.delete(existing);
    }

    private void validateRanges(LoanProductRequest request) {
        if (request.getMinimumAmount() != null && request.getMaximumAmount() != null
                && request.getMinimumAmount() > request.getMaximumAmount()) {
            throw new ValidationException("Minimum amount cannot be greater than maximum amount");
        }
        if (request.getMinimumTenure() != null && request.getMaximumTenure() != null
                && request.getMinimumTenure() > request.getMaximumTenure()) {
            throw new ValidationException("Minimum tenure cannot be greater than maximum tenure");
        }
    }

    private void apply(LoanProduct product, LoanProductRequest request) {
        product.setLoanCode(request.getLoanCode());
        product.setLoanName(request.getLoanName());
        product.setLoanType(request.getLoanType());
        product.setMinimumAmount(request.getMinimumAmount());
        product.setMaximumAmount(request.getMaximumAmount());
        product.setInterestRate(request.getInterestRate());
        product.setMinimumTenure(request.getMinimumTenure());
        product.setMaximumTenure(request.getMaximumTenure());
        product.setProcessingFee(request.getProcessingFee());
        product.setDailyPenaltyRate(request.getDailyPenaltyRate());
        product.setActive(request.getActive() != null ? request.getActive() : Boolean.TRUE);
    }
}

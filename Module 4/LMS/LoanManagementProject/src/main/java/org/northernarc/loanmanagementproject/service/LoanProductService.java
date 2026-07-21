package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.dto.request.LoanProductRequest;
import org.northernarc.loanmanagementproject.entity.LoanProduct;

import java.util.List;

/**
 * LoanProductService - Management of loan product catalogue.
 */
public interface LoanProductService {

    LoanProduct create(LoanProductRequest request);

    LoanProduct getByCode(String loanCode);

    List<LoanProduct> getAll();

    List<LoanProduct> getAllActive();

    LoanProduct update(LoanProductRequest request);

    void delete(String loanCode);
}

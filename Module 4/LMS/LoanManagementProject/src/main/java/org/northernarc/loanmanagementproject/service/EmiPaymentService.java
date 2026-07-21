package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.dto.request.EmiPaymentRequest;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentResponse;
import org.northernarc.loanmanagementproject.entity.EmiPayment;

import java.util.List;

/**
 * EmiPaymentService - EMI processing: pay an installment (with penalty,
 * principal/interest split and automatic loan closure) and expose the
 * amortization schedule.
 */
public interface EmiPaymentService {

    EmiPayment pay(EmiPaymentRequest request);

    EmiPayment getById(Long paymentId);

    List<EmiPayment> getAll();

    List<EmiPayment> getByLoanAccountId(Long loanAccountId);

    List<EmiPayment> getByPaymentType(String paymentType);

    /**
     * Projected amortization schedule for an account, marking already-paid
     * installments as PAID and the rest as PENDING. Not persisted.
     */
    List<EmiPaymentResponse> getSchedule(Long loanAccountId);
}

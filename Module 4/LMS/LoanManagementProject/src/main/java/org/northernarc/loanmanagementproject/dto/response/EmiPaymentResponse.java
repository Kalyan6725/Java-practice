package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * EmiPaymentResponse - Full view of an EMI installment/payment record, including
 * the principal/interest/penalty breakdown used for receipts and payment history.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiPaymentResponse {

    private Long paymentId;
    private Long loanAccountId;
    private String loanNumber;

    private Integer installmentNo;
    private LocalDate dueDate;
    private LocalDate paymentDate;

    private Double emiAmount;
    private Double principalPaid;
    private Double interestPaid;
    private Double penaltyPaid;
    private Double totalPaid;

    private String paymentType;
    private String status;
}

package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmiPaymentRequest - Payload for paying an EMI installment.
 *
 * installmentNo is optional: when omitted the service pays the earliest
 * PENDING/OVERDUE installment. amount is optional and defaults to the scheduled
 * EMI plus any accrued penalty computed by the service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmiPaymentRequest {

    @NotNull(message = "Loan account id is required")
    private Long loanAccountId;

    @Min(value = 1, message = "Installment number must be at least 1")
    private Integer installmentNo;

    @Positive(message = "Amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Amount cannot exceed 10,000,000")
    private Double amount;

    @NotBlank(message = "Payment type is required")
    @Pattern(regexp = "CASH|CARD|ONLINE|UPI",
            message = "Payment type must be one of: CASH, CARD, ONLINE, UPI")
    private String paymentType;
}

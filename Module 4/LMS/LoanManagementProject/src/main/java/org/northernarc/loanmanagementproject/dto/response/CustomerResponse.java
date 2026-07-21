package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerResponse - Safe representation of a customer.
 * Deliberately omits the password hash and JPA relationships so it can never
 * leak sensitive data or trigger lazy-loading serialization issues.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;
    private String customerName;
    private String email;
    private String phone;
    private String address;
    private String branch;
    private String role;
    private String status;
}

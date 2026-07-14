package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdminCustomerDTO - Used by ADMIN to bulk create/import customers
 * Different from Customer entity to enforce password encoding at controller level
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCustomerDTO {
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message = "Branch is required")
    @Size(min = 2, max = 50, message = "Branch must be between 2 and 50 characters")
    private String branch;
    
    @Pattern(regexp = "ADMIN|MANAGER|USER", message = "Role must be ADMIN, MANAGER, or USER")
    private String role = "USER";
}

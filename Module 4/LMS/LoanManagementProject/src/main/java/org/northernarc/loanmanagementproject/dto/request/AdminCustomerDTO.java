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

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must be 10 to 15 digits and may start with +")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;
    
    @Pattern(regexp = "USER|UNDERWRITER|MANAGER|ADMIN",
            message = "Role must be USER, UNDERWRITER, MANAGER, or ADMIN")
    private String role = "USER";

    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE")
    private String status = "ACTIVE";
}

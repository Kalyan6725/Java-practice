package org.northernarc.loanmanagementproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest - DTO for login request
 * 
 * Fields:
 * - email: Customer email address
 * - password: Plain text password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    private String email;
    
    private String password;
}

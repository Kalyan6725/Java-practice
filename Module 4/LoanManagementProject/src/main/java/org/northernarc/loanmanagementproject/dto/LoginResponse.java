package org.northernarc.loanmanagementproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponse - DTO for login response
 * 
 * Returns the JWT token after successful authentication
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private String token;
    
    private String type = "Bearer";
    
    private String message;
    
    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}

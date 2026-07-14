package org.northernarc.customerproduct.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerRequestDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Invalid username")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER")
    private String role;

    @NotBlank(message = "Customer name is required")
    private String name;
}

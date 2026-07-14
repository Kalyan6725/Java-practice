package org.northernarc.testingassignment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must be exactly 10 digits")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must contain only digits")
    private String phone;
}

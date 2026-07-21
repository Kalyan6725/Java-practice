package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerUpdateRequest - Safe partial-update payload for a customer.
 *
 * Notes:
 * - customerId identifies the record to update.
 * - password is optional; when null/blank the existing password is preserved.
 * - email is intentionally NOT updatable here (it is the login identity).
 * - role/status are only honoured for ADMIN callers in the service/controller layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must be 10 to 15 digits and may start with +")
    private String phone;

    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @Size(min = 2, max = 100, message = "Branch must be between 2 and 100 characters")
    private String branch;

    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Pattern(regexp = "USER|UNDERWRITER|MANAGER|ADMIN",
            message = "Role must be USER, UNDERWRITER, MANAGER, or ADMIN")
    private String role;

    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be ACTIVE or INACTIVE")
    private String status;
}

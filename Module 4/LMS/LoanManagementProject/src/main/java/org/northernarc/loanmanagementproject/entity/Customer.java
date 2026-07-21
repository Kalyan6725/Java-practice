package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Customer name cannot be blank")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone must be 10 to 15 digits and may start with +")
    @Column(name = "phone", nullable = false, length = 16)
    private String phone = "+910000000000";

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    @Column(name = "address", nullable = false, length = 255)
    private String address = "Address not provided";

    @NotBlank(message = "Branch cannot be blank")
    @Size(min = 2, max = 100, message = "Branch must be between 2 and 100 characters")
    @Column(name = "branch", nullable = false)
    private String branch;

    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "USER|UNDERWRITER|MANAGER|ADMIN",
            message = "Role must be one of: USER, UNDERWRITER, MANAGER, ADMIN")
    @Column(name = "role", nullable = false)
    private String role = "USER";

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be one of: ACTIVE, INACTIVE")
    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanApplication> loanApplications = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanAccount> loanAccounts = new ArrayList<>();
}

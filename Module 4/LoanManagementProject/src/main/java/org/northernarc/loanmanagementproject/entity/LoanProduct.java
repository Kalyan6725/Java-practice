package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanProduct {
    @Id
    @NotBlank(message = "Loan code cannot be blank")
    @Size(min = 3, max = 20, message = "Loan code must be between 3 and 20 characters")
    @Column(name = "loan_code")
    private String loanCode;

    @NotBlank(message = "Loan name cannot be blank")
    @Size(min = 2, max = 100, message = "Loan name must be between 2 and 100 characters")
    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @NotBlank(message = "Loan type cannot be blank")
    @Pattern(regexp = "PERSONAL|HOME|VEHICLE|EDUCATION|BUSINESS", 
             message = "Loan type must be one of: PERSONAL, HOME, VEHICLE, EDUCATION, BUSINESS")
    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @PositiveOrZero(message = "Daily penalty rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Daily penalty rate cannot exceed 100%")
    @Column(name = "daily_penalty_rate")
    private Double dailyPenaltyRate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "loanProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanAccount> loanAccounts = new ArrayList<>();

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

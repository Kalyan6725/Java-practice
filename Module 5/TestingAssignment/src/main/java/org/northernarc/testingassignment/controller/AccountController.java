package org.northernarc.testingassignment.controller;

import org.northernarc.testingassignment.entity.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Account());
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(new Account());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(new Account());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(new Account());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(new Account());
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest {
        @NotNull(message = "Account number is required")
        private String accountNumber;

        @NotNull(message = "Customer ID is required")
        private Long customerId;

        @NotNull(message = "Account type is required")
        private Account.AccountType accountType;

        @NotNull(message = "Balance is required")
        private Double balance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAccountRequest {
        @NotNull(message = "Balance is required")
        private Double balance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepositRequest {
        @NotNull(message = "Account ID is required")
        private Long accountId;

        @NotNull(message = "Amount is required")
        private Double amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawRequest {
        @NotNull(message = "Account ID is required")
        private Long accountId;

        @NotNull(message = "Amount is required")
        private Double amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferRequest {
        @NotNull(message = "From Account ID is required")
        private Long fromAccountId;

        @NotNull(message = "To Account ID is required")
        private Long toAccountId;

        @NotNull(message = "Amount is required")
        private Double amount;
    }
}

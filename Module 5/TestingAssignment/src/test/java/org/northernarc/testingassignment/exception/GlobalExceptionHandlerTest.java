package org.northernarc.testingassignment.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleCustomerNotFound_returnsNotFoundAndMessage() throws Exception {
        mockMvc.perform(get("/test/errors/customer-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    void handleAccountNotFound_returnsNotFoundAndMessage() throws Exception {
        mockMvc.perform(get("/test/errors/account-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Account not found"));
    }

    @Test
    void handleInsufficientBalance_returnsBadRequestAndMessage() throws Exception {
        mockMvc.perform(get("/test/errors/insufficient-balance"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient balance"));
    }

    @Test
    void handleAuthenticationException_returnsUnauthorizedAndStaticMessage() throws Exception {
        mockMvc.perform(get("/test/errors/authentication"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    void handleGeneralException_returnsBadRequestAndExceptionMessage() throws Exception {
        mockMvc.perform(get("/test/errors/general"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid state"));
    }

    @Test
    void handleValidationException_returnsBadRequestWithFieldErrors() throws Exception {
        mockMvc.perform(post("/test/errors/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/errors/customer-not-found")
        void throwCustomerNotFound() {
            throw new CustomerNotFoundException("Customer not found");
        }

        @GetMapping("/test/errors/account-not-found")
        void throwAccountNotFound() {
            throw new AccountNotFoundException("Account not found");
        }

        @GetMapping("/test/errors/insufficient-balance")
        void throwInsufficientBalance() {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        @GetMapping("/test/errors/authentication")
        void throwAuthenticationException() {
            throw new BadCredentialsException("Bad credentials");
        }

        @GetMapping("/test/errors/general")
        void throwGeneralException() {
            throw new IllegalStateException("Invalid state");
        }

        @PostMapping("/test/errors/validation")
        void throwValidation(@Valid @RequestBody ValidationRequest request) {
        }
    }

    static class ValidationRequest {
        @NotBlank(message = "Name is required")
        private final String name;

        @JsonCreator
        ValidationRequest(@JsonProperty("name") String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

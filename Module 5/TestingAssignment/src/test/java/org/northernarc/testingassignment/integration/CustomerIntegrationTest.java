package org.northernarc.testingassignment.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.northernarc.testingassignment.dto.LoginRequest;
import org.northernarc.testingassignment.dto.RegisterRequest;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class CustomerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        
        customerRepository.deleteAll();

        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setName("Integration Test User");
        validRegisterRequest.setEmail("integration" + System.currentTimeMillis() + "@test.com");
        validRegisterRequest.setPhone("9876543210");
        validRegisterRequest.setPassword("IntegrationPass@123");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail(validRegisterRequest.getEmail());
        validLoginRequest.setPassword(validRegisterRequest.getPassword());
    }

    @Test
    public void registerNewCustomer_withValidData_createsCustomerSuccessfully() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        assert customerRepository.findByEmail(validRegisterRequest.getEmail()).isPresent();
    }

    @Test
    public void registerDuplicateCustomer_withSameEmail_returns409Conflict() throws Exception {
        // First registration
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        // Duplicate registration
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerCustomer_withInvalidEmail_returns400BadRequest() throws Exception {
        validRegisterRequest.setEmail("invalid-email");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerCustomer_withInvalidPhoneLength_returns400BadRequest() throws Exception {
        validRegisterRequest.setPhone("123456");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerCustomer_withMissingName_returns400BadRequest() throws Exception {
        validRegisterRequest.setName(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerAndLogin_withValidCredentials_successfulAuthentication() throws Exception {
        // Register customer
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        // Login with registered credentials
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void getCustomer_withValidToken_returnsCustomerData() throws Exception {
        // Register customer
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        // Get customer by ID (assumes service returns customer after registration)
        mockMvc.perform(get("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void registerCustomer_passwordIsEncrypted_notStoredInPlainText() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        Customer customer = customerRepository.findByEmail(validRegisterRequest.getEmail()).orElse(null);
        assert customer != null;
        assert !customer.getPassword().equals(validRegisterRequest.getPassword());
    }

    @Test
    public void updateCustomer_withValidData_updatesSuccessfully() throws Exception {
        // Register first
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        // Update customer
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail(validRegisterRequest.getEmail());
        updateRequest.setPhone("9876543210");
        updateRequest.setPassword("UpdatedPass@123");

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCustomer_removesCustomerFromDatabase() throws Exception {
        // Register first
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());

        long initialCount = customerRepository.count();

        // Delete customer
        mockMvc.perform(delete("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assert customerRepository.count() < initialCount;
    }

    @Test
    public void getAllCustomers_returnsListOfAllCustomers() throws Exception {
        // Register multiple customers
        for (int i = 0; i < 3; i++) {
            RegisterRequest request = new RegisterRequest();
            request.setName("Customer " + i);
            request.setEmail("customer" + i + System.currentTimeMillis() + "@test.com");
            request.setPhone("9876543210");
            request.setPassword("Password@123");

            mockMvc.perform(post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        // Get all customers
        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }
}

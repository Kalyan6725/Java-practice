package org.northernarc.loanmanagementproject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.northernarc.loanmanagementproject.dto.request.EmiPaymentRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanApplicationReviewRequest;
import org.northernarc.loanmanagementproject.dto.request.LoanProductRequest;
import org.northernarc.loanmanagementproject.dto.request.RegisterRequest;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.repository.EmiPaymentRepository;
import org.northernarc.loanmanagementproject.repository.LoanAccountRepository;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the modular Loan Management API. Covers authentication,
 * role-based access, product management, JPA queries and the end-to-end loan
 * workflow (apply -> review -> approve -> disburse -> pay -> close).
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class SecureLoanManagementApiApplicationTests {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private LoanProductRepository loanProductRepository;
    @Autowired private LoanAccountRepository loanAccountRepository;
    @Autowired private EmiPaymentRepository emiPaymentRepository;
    @Autowired private EntityManager entityManager;

    private static final String USER_EMAIL = "rahul.sharma@example.com";

    private Customer testCustomer;
    private LoanProduct testProduct1;
    private LoanProduct testProduct2;

    @BeforeEach
    void setUpData() {
        emiPaymentRepository.deleteAllInBatch();
        loanAccountRepository.deleteAllInBatch();
        loanProductRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();

        testCustomer = new Customer();
        testCustomer.setCustomerName("Rahul Sharma");
        testCustomer.setEmail(USER_EMAIL);
        testCustomer.setPassword(passwordEncoder.encode("password123"));
        testCustomer.setPhone("+919812345678");
        testCustomer.setAddress("12 MG Road, Hyderabad");
        testCustomer.setBranch("Hyderabad");
        testCustomer.setRole("USER");
        testCustomer.setStatus("ACTIVE");
        testCustomer = customerRepository.save(testCustomer);

        testProduct1 = buildProduct("LP001", "Personal Prime", "PERSONAL", 2.5);
        testProduct1 = loanProductRepository.save(testProduct1);

        testProduct2 = buildProduct("LP002", "Home Secure", "HOME", 1.0);
        testProduct2 = loanProductRepository.save(testProduct2);
    }

    private LoanProduct buildProduct(String code, String name, String type, double penalty) {
        LoanProduct p = new LoanProduct();
        p.setLoanCode(code);
        p.setLoanName(name);
        p.setLoanType(type);
        p.setMinimumAmount(10000.0);
        p.setMaximumAmount(1000000.0);
        p.setInterestRate(12.0);
        p.setMinimumTenure(12);
        p.setMaximumTenure(60);
        p.setProcessingFee(500.0);
        p.setDailyPenaltyRate(penalty);
        p.setActive(true);
        return p;
    }

    private LoanProductRequest productRequest(String code, String name, String type, double penalty) {
        LoanProductRequest r = new LoanProductRequest();
        r.setLoanCode(code);
        r.setLoanName(name);
        r.setLoanType(type);
        r.setMinimumAmount(10000.0);
        r.setMaximumAmount(1000000.0);
        r.setInterestRate(12.0);
        r.setMinimumTenure(12);
        r.setMaximumTenure(60);
        r.setProcessingFee(500.0);
        r.setDailyPenaltyRate(penalty);
        r.setActive(true);
        return r;
    }

    private LoanAccount createLoanAccount(Customer c, LoanProduct p, String status, double amount) {
        Customer managedCustomer = customerRepository.findById(c.getCustomerId()).orElseThrow();
        LoanProduct managedProduct = loanProductRepository.findById(p.getLoanCode()).orElseThrow();
        LoanAccount la = new LoanAccount();
        la.setLoanStartDate(LocalDate.now().minusDays(10));
        la.setEmiDueDate(LocalDate.now().plusDays(20));
        la.setStatus(status);
        la.setLoanAmount(amount);
        la.setEmiAmount(5000.0);
        la.setCustomer(managedCustomer);
        la.setLoanProduct(managedProduct);
        return loanAccountRepository.save(la);
    }

    private EmiPayment createPayment(LoanAccount la, double paid, double penalty, LocalDate date) {
        EmiPayment ep = new EmiPayment();
        ep.setAmountPaid(paid);
        ep.setPenaltyPaid(penalty);
        ep.setPaymentType("ONLINE");
        ep.setPaymentDate(date);
        ep.setLoanAccount(la);
        return emiPaymentRepository.save(ep);
    }

    private JsonNode data(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString()).path("data");
    }

    // ============================ Entity / JPA ============================

    @Nested
    @DisplayName("Entity Relationship & Cascade")
    class EntityMappingTests {
        @Test
        @DisplayName("Deleting a customer cascades to accounts and payments")
        void testCascadeDeleteCustomer() {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 200000.0);
            EmiPayment ep = createPayment(la, 10000.0, 250.0, LocalDate.now());
            Long accountId = la.getLoanAccountId();
            Long paymentId = ep.getPaymentId();

            entityManager.flush();
            entityManager.clear();
            customerRepository.deleteById(testCustomer.getCustomerId());
            customerRepository.flush();

            assertThat(loanAccountRepository.findById(accountId)).isEmpty();
            assertThat(emiPaymentRepository.findById(paymentId)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Spring Data JPA queries")
    class QueryTests {
        @Test
        @DisplayName("Derived and JPQL queries return expected data")
        void testQueries() {
            List<LoanProduct> personal = loanProductRepository.findByLoanType("PERSONAL");
            assertThat(personal).hasSize(1);

            List<Customer> hyderabad = customerRepository.findByBranch("Hyderabad");
            assertThat(hyderabad).hasSize(1);

            List<LoanProduct> highPenalty = loanProductRepository.findByDailyPenaltyRateGreaterThan(2.0);
            assertThat(highPenalty).extracting(LoanProduct::getLoanCode).containsExactly("LP001");
        }

        @Test
        @DisplayName("Total penalty per branch aggregates via JOIN/GROUP BY")
        void testPenaltyPerBranch() {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 200000.0);
            createPayment(la, 5000.0, 120.0, LocalDate.now());
            entityManager.flush();

            List<Object[]> rows = loanAccountRepository.findTotalPenaltyPerBranch();
            assertThat(rows).isNotEmpty();
            assertThat(rows.get(0)[0]).isEqualTo("Hyderabad");
            assertThat(((Number) rows.get(0)[1]).doubleValue()).isEqualTo(120.0);
        }

        @Test
        @DisplayName("Modifying JPQL updates penalty rate by loan type")
        void testIncreasePenalty() {
            int updated = loanProductRepository.increaseDailyPenaltyRates("PERSONAL", 1.5);
            assertThat(updated).isEqualTo(1);
            loanProductRepository.flush();
            entityManager.clear();
            assertThat(loanProductRepository.findById("LP001").orElseThrow().getDailyPenaltyRate()).isEqualTo(4.0);
            assertThat(loanProductRepository.findById("LP002").orElseThrow().getDailyPenaltyRate()).isEqualTo(1.0);
        }
    }

    // ============================ Validation ============================

    @Nested
    @DisplayName("Bean Validation")
    class ValidationTests {
        @Test
        @DisplayName("Invalid registration payload returns 400 field errors")
        void testRegisterValidation() throws Exception {
            RegisterRequest invalid = new RegisterRequest();
            invalid.setCustomerName("");
            invalid.setEmail("bad-email");
            invalid.setPassword("123");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("FIELD_VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("Negative penalty rate on product create returns 400")
        void testNegativePenaltyRate() throws Exception {
            LoanProductRequest invalid = productRequest("LP009", "Invalid Rate", "PERSONAL", -1.0);
            mockMvc.perform(post("/api/loan-products")
                            .with(user("admin@test.com").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ============================ Auth / Security ============================

    @Nested
    @DisplayName("JWT Authentication & Authorization")
    class AuthTests {
        @Test
        @DisplayName("Login returns a JWT for valid credentials")
        void testLogin() throws Exception {
            Map<String, String> login = Map.of("email", USER_EMAIL, "password", "password123");
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.token").exists())
                    .andExpect(jsonPath("$.data.token", startsWith("eyJ")));
        }

        @Test
        @DisplayName("Unauthenticated access to a secured endpoint is rejected")
        void testUnauthenticated() throws Exception {
            mockMvc.perform(get("/api/loan-products").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("USER cannot delete a loan product")
        void testUserCannotDelete() throws Exception {
            mockMvc.perform(delete("/api/loan-products/{code}", "LP001")
                            .with(user("user@test.com").roles("USER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("ADMIN can delete a loan product")
        void testAdminCanDelete() throws Exception {
            mockMvc.perform(delete("/api/loan-products/{code}", "LP001")
                            .with(user("admin@test.com").roles("ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("MANAGER can update a loan product")
        void testManagerCanUpdate() throws Exception {
            LoanProductRequest payload = productRequest("LP002", "Home Secure Updated", "HOME", 2.0);
            mockMvc.perform(put("/api/loan-products")
                            .with(user("manager@test.com").roles("MANAGER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk());
        }
    }

    // ============================ Pagination / DTO ============================

    @Nested
    @DisplayName("Pagination & DTO mapping")
    class PaginationDtoTests {
        @Test
        @DisplayName("Paged products are sorted by penalty rate desc")
        void testPagedProducts() throws Exception {
            mockMvc.perform(get("/api/loan-products/paged")
                            .with(user("user@test.com").roles("USER"))
                            .param("page", "0").param("size", "10")
                            .param("sortBy", "dailyPenaltyRate").param("direction", "DESC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.items", hasSize(2)))
                    .andExpect(jsonPath("$.data.items[0].loanCode").value("LP001"))
                    .andExpect(jsonPath("$.data.items[1].loanCode").value("LP002"));
        }

        @Test
        @DisplayName("Customer summary DTO maps aggregates")
        void testCustomerSummary() throws Exception {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 400000.0);
            createPayment(la, 10000.0, 300.0, LocalDate.now());
            entityManager.flush();

            mockMvc.perform(get("/api/customers/{id}/summary", testCustomer.getCustomerId())
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.customerName").value("Rahul Sharma"))
                    .andExpect(jsonPath("$.data.branch").value("Hyderabad"))
                    .andExpect(jsonPath("$.data.numberOfLoans").value(1))
                    .andExpect(jsonPath("$.data.totalLoanAmount").value(400000.0))
                    .andExpect(jsonPath("$.data.totalPenaltyPaid").value(300.0));
        }
    }

    // ============================ Exceptions ============================

    @Nested
    @DisplayName("Global Exception Handling")
    class ExceptionTests {
        @Test
        @DisplayName("Missing customer returns standardized 404")
        void testCustomerNotFound() throws Exception {
            mockMvc.perform(get("/api/customers/{id}", 999999L)
                            .with(user("manager@test.com").roles("MANAGER")))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error.code").value("CUSTOMER_NOT_FOUND"))
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    // ============================ Dashboard ============================

    @Nested
    @DisplayName("Dashboard aggregation")
    class DashboardTests {
        @Test
        @DisplayName("Dashboard returns aggregate metrics")
        void testDashboard() throws Exception {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 600000.0);
            createPayment(la, 15000.0, 500.0, LocalDate.now());
            entityManager.flush();

            mockMvc.perform(get("/api/dashboard")
                            .with(user("manager@test.com").roles("MANAGER")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.totalCustomers").value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.data.totalPenaltyCollected").value(greaterThanOrEqualTo(500.0)))
                    .andExpect(jsonPath("$.data.topBranch").value("Hyderabad"));
        }
    }

    // ============================ Full workflow ============================

    @Nested
    @DisplayName("End-to-end loan workflow")
    class WorkflowTests {
        @Test
        @DisplayName("apply -> approve -> disburse -> pay creates an active then serviced loan")
        void testFullWorkflow() throws Exception {
            // 1. USER applies
            LoanApplicationRequest applyReq = new LoanApplicationRequest();
            applyReq.setLoanCode("LP001");
            applyReq.setRequestedAmount(240000.0);
            applyReq.setTenureMonths(24);

            MvcResult applyResult = mockMvc.perform(post("/api/loan-applications")
                            .with(user(USER_EMAIL).roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(applyReq)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.status").value("SUBMITTED"))
                    .andReturn();
            long applicationId = data(applyResult).path("applicationId").asLong();

            // 2. UNDERWRITER approves
            LoanApplicationReviewRequest review = new LoanApplicationReviewRequest();
            review.setDecision("APPROVED");
            review.setRemarks("Looks good");
            mockMvc.perform(put("/api/loan-applications/{id}/review", applicationId)
                            .with(user("uw@test.com").roles("UNDERWRITER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(review)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.status").value("APPROVED"));

            // 3. An APPROVED account now exists
            MvcResult accountsResult = mockMvc.perform(get("/api/loan-accounts")
                            .with(user("manager@test.com").roles("MANAGER")))
                    .andExpect(status().isOk())
                    .andReturn();
            JsonNode accounts = data(accountsResult);
            assertThat(accounts).isNotEmpty();
            long accountId = accounts.get(0).path("loanAccountId").asLong();
            assertThat(accounts.get(0).path("status").asText()).isEqualTo("APPROVED");

            // 4. MANAGER disburses -> ACTIVE
            mockMvc.perform(post("/api/loan-accounts/{id}/disburse", accountId)
                            .with(user("manager@test.com").roles("MANAGER")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.status").value("ACTIVE"));

            // 5. USER pays the first EMI
            EmiPaymentRequest pay = new EmiPaymentRequest();
            pay.setLoanAccountId(accountId);
            pay.setPaymentType("UPI");
            mockMvc.perform(post("/api/emi-payments")
                            .with(user(USER_EMAIL).roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(pay)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.status").value("PAID"))
                    .andExpect(jsonPath("$.data.installmentNo").value(1));

            // 6. Schedule has one row per installment
            mockMvc.perform(get("/api/emi-payments/account/{id}/schedule", accountId)
                            .with(user(USER_EMAIL).roles("USER")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(24)))
                    .andExpect(jsonPath("$.data[0].status").value("PAID"));
        }

        @Test
        @DisplayName("Applying above the product maximum is rejected")
        void testApplyExceedsMax() throws Exception {
            LoanApplicationRequest applyReq = new LoanApplicationRequest();
            applyReq.setLoanCode("LP001");
            applyReq.setRequestedAmount(5000000.0); // above max 1,000,000
            applyReq.setTenureMonths(24);

            mockMvc.perform(post("/api/loan-applications")
                            .with(user(USER_EMAIL).roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(applyReq)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
        }
    }
}

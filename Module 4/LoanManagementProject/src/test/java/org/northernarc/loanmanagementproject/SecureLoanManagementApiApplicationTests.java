package org.northernarc.loanmanagementproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

    private Customer testCustomer;
    private LoanProduct testProduct1;
    private LoanProduct testProduct2;

    @BeforeEach
    void setUpData() {
        emiPaymentRepository.deleteAllInBatch();
        loanAccountRepository.deleteAllInBatch();
        loanProductRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        emiPaymentRepository.flush();
        loanAccountRepository.flush();
        loanProductRepository.flush();
        customerRepository.flush();

        testCustomer = new Customer();
        testCustomer.setCustomerName("Rahul Sharma");
        testCustomer.setEmail("rahul.sharma@example.com");
        testCustomer.setPassword(passwordEncoder.encode("password123"));
        testCustomer.setBranch("Hyderabad");
        testCustomer.setRole("USER");
        testCustomer = customerRepository.save(testCustomer);

        testProduct1 = new LoanProduct();
        testProduct1.setLoanCode("LP001");
        testProduct1.setLoanName("Personal Prime");
        testProduct1.setLoanType("PERSONAL");
        testProduct1.setDailyPenaltyRate(2.5);
        testProduct1 = loanProductRepository.save(testProduct1);

        testProduct2 = new LoanProduct();
        testProduct2.setLoanCode("LP002");
        testProduct2.setLoanName("Home Secure");
        testProduct2.setLoanType("HOME");
        testProduct2.setDailyPenaltyRate(1.0);
        testProduct2 = loanProductRepository.save(testProduct2);
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

    @Nested
    @DisplayName("Task 1: Entity Relationship & Cascade Validation")
    class EntityMappingTests {
        @Test
        @DisplayName("Should cascade delete LoanAccounts and EmiPayments when Customer is deleted")
        void testCascadeDeleteCustomer() {
            Customer managedCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElseThrow();
            LoanProduct managedProduct = loanProductRepository.findById(testProduct1.getLoanCode()).orElseThrow();

            LoanAccount la = new LoanAccount();
            la.setLoanStartDate(LocalDate.now().minusDays(10));
            la.setEmiDueDate(LocalDate.now().plusDays(20));
            la.setStatus("ACTIVE");
            la.setLoanAmount(200000.0);
            la.setEmiAmount(5000.0);
            la.setCustomer(managedCustomer);
            la.setLoanProduct(managedProduct);
            EmiPayment ep = new EmiPayment();
            ep.setAmountPaid(10000.0);
            ep.setPenaltyPaid(250.0);
            ep.setPaymentType("ONLINE");
            ep.setPaymentDate(LocalDate.now());
            LoanAccount savedLoanAccount = loanAccountRepository.saveAndFlush(la);
            ep.setLoanAccount(savedLoanAccount);
            EmiPayment savedPayment = emiPaymentRepository.saveAndFlush(ep);

            Long loanAccountId = savedLoanAccount.getLoanAccountId();
            Long paymentId = savedPayment.getPaymentId();

            entityManager.clear();
            customerRepository.deleteById(testCustomer.getCustomerId());
            customerRepository.flush();

            assertThat(loanAccountRepository.findById(loanAccountId)).isEmpty();
            assertThat(emiPaymentRepository.findById(paymentId)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Task 2: Bean Validation Unit & API Constraints")
    class ValidationTests {
        @Test
        @DisplayName("Should return 400 when register payload violates constraints")
        void testCustomerValidationConstraints() throws Exception {
            Customer invalid = new Customer();
            invalid.setCustomerName("");
            invalid.setEmail("bad-email");
            invalid.setPassword("123");
            invalid.setBranch("");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.errorType").value("FIELD_VALIDATION_ERROR"));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("Should return 400 when daily penalty rate is negative")
        void testNegativePenaltyRateValidation() throws Exception {
            LoanProduct invalid = new LoanProduct();
            invalid.setLoanCode("LP009");
            invalid.setLoanName("Invalid Rate Loan");
            invalid.setLoanType("PERSONAL");
            invalid.setDailyPenaltyRate(-1.0);

            mockMvc.perform(post("/api/loan/product/create")
                            .with(user("admin@test.com").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Task 3: Spring Data JPA Derived Query Asserts")
    class DerivedQueryTests {
        @Test
        @DisplayName("Verify derived queries work")
        void testDerivedQueries() {
            List<LoanProduct> personal = loanProductRepository.findByLoanType("PERSONAL");
            assertThat(personal).hasSize(1);

            List<Customer> hyderabadCustomers = customerRepository.findByBranch("Hyderabad");
            assertThat(hyderabadCustomers).hasSize(1);

            List<LoanProduct> highPenalty = loanProductRepository.findByDailyPenaltyRateGreaterThan(2.0);
            assertThat(highPenalty).hasSize(1);
            assertThat(highPenalty.get(0).getLoanCode()).isEqualTo("LP001");
        }
    }

    @Nested
    @DisplayName("Task 4: Complex JPQL Query Operations")
    class JpqlQueryTests {
        @Test
        @DisplayName("JPQL: Find premium borrowers")
        void testFindPremiumBorrowers() {
            createLoanAccount(testCustomer, testProduct1, "ACTIVE", 300000.0);
            createLoanAccount(testCustomer, testProduct2, "ACTIVE", 500000.0);

            List<Customer> premium = customerRepository.findPremiumBorrowers(1L);
            assertThat(premium).hasSize(1);
            assertThat(premium.get(0).getCustomerName()).isEqualTo("Rahul Sharma");
        }

        @Test
        @DisplayName("JPQL: Total penalty per branch")
        void testFindTotalPenaltyPerBranch() {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 200000.0);
            createPayment(la, 5000.0, 120.0, LocalDate.now());

            List<Object[]> rows = loanAccountRepository.findTotalPenaltyPerBranch();
            assertThat(rows).isNotEmpty();
            assertThat(rows.get(0)[0]).isEqualTo("Hyderabad");
            assertThat(((Number) rows.get(0)[1]).doubleValue()).isEqualTo(120.0);
        }

        @Test
        @DisplayName("JPQL: Customers with multiple loan types")
        void testFindCustomersWithMultipleLoanTypes() {
            createLoanAccount(testCustomer, testProduct1, "ACTIVE", 300000.0);
            createLoanAccount(testCustomer, testProduct2, "ACTIVE", 700000.0);

            List<Customer> customers = customerRepository.findCustomersWithMultipleLoanTypes();
            assertThat(customers).hasSize(1);
        }

        @Test
        @DisplayName("JPQL: Latest EMI payment")
        void testFindLatestEmiPayment() {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 200000.0);
            createPayment(la, 1000.0, 50.0, LocalDate.now().minusDays(2));
            createPayment(la, 2000.0, 60.0, LocalDate.now());

            List<EmiPayment> latest = emiPaymentRepository.findLatestPaymentOrderByDesc();
            assertThat(latest).isNotEmpty();
            assertThat(latest.get(0).getPaymentDate()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("JPQL: Loan products with no overdue history")
        void testFindProductsWithNoOverdueHistory() {
            createLoanAccount(testCustomer, testProduct1, "OVERDUE", 250000.0);

            List<LoanProduct> cleanProducts = loanProductRepository.findProductsWithNoOverdueHistory();
            assertThat(cleanProducts).extracting(LoanProduct::getLoanCode).contains("LP002");
            assertThat(cleanProducts).extracting(LoanProduct::getLoanCode).doesNotContain("LP001");
        }
    }

    @Nested
    @DisplayName("Task 5: Modifying JPQL Update Invocations")
    class JpqlUpdateTests {
        @Test
        @DisplayName("Increase penalty rates by loan type")
        void testIncreaseDailyPenaltyRates() {
            int updated = loanProductRepository.increaseDailyPenaltyRates("PERSONAL", 1.5);
            assertThat(updated).isEqualTo(1);

            loanProductRepository.flush();
            entityManager.clear();
            LoanProduct lp1 = loanProductRepository.findById("LP001").orElseThrow();
            LoanProduct lp2 = loanProductRepository.findById("LP002").orElseThrow();

            assertThat(lp1.getDailyPenaltyRate()).isEqualTo(4.0);
            assertThat(lp2.getDailyPenaltyRate()).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Task 6: API Pagination & Sorting")
    class PaginationTests {
        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("GET /loan-products should return paged results sorted by dailyPenaltyRate desc")
        void testGetLoanProductsPaginatedAndSorted() throws Exception {
            mockMvc.perform(get("/api/loan/loan-products")
                            .with(user("user@test.com").roles("USER"))
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "dailyPenaltyRate")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].loanCode").value("LP001"))
                    .andExpect(jsonPath("$.content[1].loanCode").value("LP002"));
        }
    }

    @Nested
    @DisplayName("Task 7: DTO Projection Mapping")
    class DtoMappingTests {
        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Customer summary DTO should map correctly")
        void testCustomerSummaryDtoMapping() throws Exception {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 400000.0);
            createPayment(la, 10000.0, 300.0, LocalDate.now());

            mockMvc.perform(get("/api/loan/customer/{customerId}/summary", testCustomer.getCustomerId())
                            .with(user("user@test.com").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.customerName").value("Rahul Sharma"))
                    .andExpect(jsonPath("$.branch").value("Hyderabad"))
                    .andExpect(jsonPath("$.numberOfLoans").value(1))
                    .andExpect(jsonPath("$.totalLoanAmount").value(400000.0))
                    .andExpect(jsonPath("$.totalPenaltyPaid").value(300.0));
        }
    }

    @Nested
    @DisplayName("Task 8: JWT Authentication")
    class JwtAuthenticationTests {
        @Test
        @DisplayName("POST /login should return JWT for valid credentials")
        void testSuccessfulLogin() throws Exception {
            Map<String, String> login = Map.of(
                    "email", "rahul.sharma@example.com",
                    "password", "password123"
            );

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(login)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token", startsWith("eyJ")));
        }

        @Test
        @DisplayName("Unauthenticated call to secured API should return 403")
        void testUnauthenticatedAccessFails() throws Exception {
            mockMvc.perform(get("/api/loan/products")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Task 9: Role-Based Authorization")
    class AuthorizationTests {
        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("USER cannot delete loan product")
        void testUserCannotDeleteLoanProduct() throws Exception {
            mockMvc.perform(delete("/api/loan/product/{loanCode}", "LP001")
                            .with(user("user@test.com").roles("USER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("ADMIN can delete loan product")
        void testAdminCanDeleteLoanProduct() throws Exception {
            mockMvc.perform(delete("/api/loan/product/{loanCode}", "LP001")
                            .with(user("admin@test.com").roles("ADMIN")))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("MANAGER can update loan product")
        void testManagerCanUpdateLoanProduct() throws Exception {
            LoanProduct payload = new LoanProduct();
            payload.setLoanCode("LP002");
            payload.setLoanName("Home Secure Updated");
            payload.setLoanType("HOME");
            payload.setDailyPenaltyRate(2.0);

            mockMvc.perform(put("/api/loan/product/update")
                            .with(user("manager@test.com").roles("MANAGER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Task 10: Global Exception Handling")
    class ExceptionHandlingTests {
        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Missing customer should return standardized 404 response")
        void testCustomerNotFoundExceptionHandling() throws Exception {
            mockMvc.perform(get("/api/loan/customer/{customerId}", 999999L)
                            .with(user("user@test.com").roles("USER")))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.statusCode").value(404))
                    .andExpect(jsonPath("$.errorType").value("CUSTOMER_NOT_FOUND"))
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("Final Challenge: Dashboard Aggregation")
    class FinalChallengeTests {
        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("GET /dashboard should return aggregate metrics")
        void testGetDashboardMetrics() throws Exception {
            LoanAccount la = createLoanAccount(testCustomer, testProduct1, "ACTIVE", 600000.0);
            createPayment(la, 15000.0, 500.0, LocalDate.now());

            mockMvc.perform(get("/api/loan/dashboard")
                            .with(user("manager@test.com").roles("MANAGER"))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.totalCustomers").value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.data.totalLoans").value(greaterThanOrEqualTo(1)))
                    .andExpect(jsonPath("$.data.totalLoanAmountDisbursed").value(greaterThanOrEqualTo(600000.0)))
                    .andExpect(jsonPath("$.data.totalPenaltyCollected").value(greaterThanOrEqualTo(500.0)))
                    .andExpect(jsonPath("$.data.topBranch").value("Hyderabad"))
                    .andExpect(jsonPath("$.data.highestLoanCustomer").value("Rahul Sharma"));
        }
    }
}

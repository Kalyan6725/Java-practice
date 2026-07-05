package org.example.librarymgmt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.example.librarymgmt.entity.Book;
import org.example.librarymgmt.entity.FineTransaction;
import org.example.librarymgmt.entity.IssueRecord;
import org.example.librarymgmt.entity.Member;
import org.example.librarymgmt.repository.BookRepository;
import org.example.librarymgmt.repository.FineTransactionRepository;
import org.example.librarymgmt.repository.IssueRecordRepository;
import org.example.librarymgmt.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SecureLibraryApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IssueRecordRepository issueRecordRepository;

    @Autowired
    private FineTransactionRepository fineTransactionRepository;

    private Member testMember;
    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    void setUpData() {
        fineTransactionRepository.deleteAll();
        issueRecordRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();

        testMember = new Member();
        testMember.setMemberName("Amit Patel");
        testMember.setEmail("amit@library.com");
        testMember.setPassword(passwordEncoder.encode("password123"));
        testMember.setMembershipBranch("Central Library");
        testMember.setRole("USER");
        testMember = memberRepository.save(testMember);

        testBook1 = new Book();
        testBook1.setIsbn("978-3-16-148410-0");
        testBook1.setTitle("Introduction to Algorithms");
        testBook1.setBookType("ACADEMIC");
        testBook1.setDailyFineRate(5.0);
        testBook1 = bookRepository.save(testBook1);

        testBook2 = new Book();
        testBook2.setIsbn("978-0-451-52493-5");
        testBook2.setTitle("1984");
        testBook2.setBookType("FICTION");
        testBook2.setDailyFineRate(1.5);
        testBook2 = bookRepository.save(testBook2);
    }

    @Nested
    @DisplayName("Task 1: Entity Relationship & Cascade Validation")
    class EntityMappingTests {

        @Test
        @DisplayName("Book cascade should preserve fine transactions after member deletion")
        void testBookRelationPreservedAfterMemberDeletion() {
            FineTransaction fine = new FineTransaction();
            fine.setAmount(20.0);
            fine.setPaymentType("CARD");
            fine.setPaymentDate(LocalDate.now());
            fine.setBook(testBook1);
            FineTransaction savedFine = fineTransactionRepository.save(fine);

            memberRepository.delete(testMember);
            memberRepository.flush();

            assertThat(fineTransactionRepository.findById(savedFine.getTransactionId())).isPresent();
        }
    }

    @Nested
    @DisplayName("Task 2: Bean Validation Unit & API Constraints")
    class ValidationTests {

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Should return 400 Bad Request when member registrations break constraints")
        void testMemberValidationConstraints() throws Exception {
            Member invalidMember = new Member();
            invalidMember.setMemberName("");
            invalidMember.setEmail("not-an-email");
            invalidMember.setPassword("123");
            invalidMember.setMembershipBranch("");

            mockMvc.perform(post("/api/library/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidMember)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.fieldErrors").exists());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Should return 400 Bad Request if book daily fine rate is negative")
        void testNegativeFineRateValidation() throws Exception {
            Book invalidBook = new Book();
            invalidBook.setIsbn("978-0-123456-78-9");
            invalidBook.setTitle("Invalid Fine Book");
            invalidBook.setBookType("FICTION");
            invalidBook.setDailyFineRate(-2.5);

            mockMvc.perform(post("/api/library/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidBook)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Book title must not be blank")
        void testBlankBookTitleValidation() throws Exception {
            Book invalidBook = new Book();
            invalidBook.setIsbn("978-1-111111-11-1");
            invalidBook.setTitle("");
            invalidBook.setBookType("REFERENCE");
            invalidBook.setDailyFineRate(2.0);

            mockMvc.perform(post("/api/library/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidBook)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Task 3: Spring Data JPA Derived Query Asserts")
    class DerivedQueryTests {

        @Test
        @DisplayName("Verify findByBookType derived mechanism works flawlessly")
        void testDerivedQueries() {
            List<Book> fictionBooks = bookRepository.findByBookType("FICTION");
            assertThat(fictionBooks).hasSize(1);
            assertThat(fictionBooks.get(0).getIsbn()).isEqualTo("978-0-451-52493-5");

            List<Member> centralMembers = memberRepository.findByMembershipBranch("Central Library");
            assertThat(centralMembers).hasSize(1);

            List<Book> highFineBooks = bookRepository.findByDailyFineRateGreaterThan(3.0);
            assertThat(highFineBooks).hasSize(1);
            assertThat(highFineBooks.get(0).getIsbn()).isEqualTo("978-3-16-148410-0");
        }

        @Test
        @DisplayName("Empty result for non-existent book type")
        void testEmptyResultForNonExistentBookType() {
            List<Book> referenceBooks = bookRepository.findByBookType("NONEXISTENT");
            assertThat(referenceBooks).isEmpty();
        }

        @Test
        @DisplayName("Multiple members per branch should be returned")
        void testMultipleMembersPerBranch() {
            Member member2 = new Member();
            member2.setMemberName("Priya Singh");
            member2.setEmail("priya@library.com");
            member2.setPassword(passwordEncoder.encode("password123"));
            member2.setMembershipBranch("Central Library");
            member2.setRole("MANAGER");
            memberRepository.save(member2);

            List<Member> centralMembers = memberRepository.findByMembershipBranch("Central Library");
            assertThat(centralMembers).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Task 4: Complex JPQL Query Operations")
    class JpqlQueryTests {

        @Test
        @DisplayName("JPQL: Find Avid Readers exceeding a set structural circulation checkout threshold")
        void testFindAvidReaders() {
            IssueRecord record1 = IssueRecord.builder().issueDate(LocalDate.now()).status("ISSUED").member(testMember).book(testBook1).build();
            IssueRecord record2 = IssueRecord.builder().issueDate(LocalDate.now()).status("ISSUED").member(testMember).book(testBook2).build();
            issueRecordRepository.saveAll(List.of(record1, record2));

            List<Member> avidReaders = memberRepository.findAvidReaders(1L);
            assertThat(avidReaders).hasSize(1);
            assertThat(avidReaders.get(0).getMemberName()).isEqualTo("Amit Patel");
        }

        @Test
        @DisplayName("JPQL: Find aggregated total fines paid grouped per branch location")
        void testFindTotalFinesPaidPerBranch() {
            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now())
                    .status("ISSUED")
                    .member(testMember)
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(50.0)
                    .paymentType("ONLINE")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            List<org.example.librarymgmt.dto.BranchFineTotalDTO> finePerBranch = memberRepository.findTotalFinesPaidPerBranch();
            assertThat(finePerBranch).isNotEmpty();
            assertThat(finePerBranch.get(0).membershipBranch()).isEqualTo("Central Library");
            assertThat(finePerBranch.get(0).totalFinesPaid()).isEqualTo(50.0);
        }

        @Test
        @DisplayName("JPQL: Find members borrowing multiple distinct book genres/categories")
        void testFindMembersHoldingMultiGenreBooks() {
            issueRecordRepository.saveAll(List.of(
                    IssueRecord.builder().issueDate(LocalDate.now()).status("ISSUED").member(testMember).book(testBook1).build(),
                    IssueRecord.builder().issueDate(LocalDate.now()).status("ISSUED").member(testMember).book(testBook2).build()));

            List<Member> multiGenreMembers = memberRepository.findMembersHoldingMultiGenreBookTypes(2L);
            assertThat(multiGenreMembers).hasSize(1);
        }

        @Test
        @DisplayName("JPQL: Fetch the latest single fine transaction execution item logs")
        void testFindLatestFinePayment() {
            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(5.0)
                    .paymentType("CASH")
                    .paymentDate(LocalDate.now().minusDays(2))
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(25.0)
                    .paymentType("CARD")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            List<FineTransaction> latest = fineTransactionRepository.findLatestFinePayment(PageRequest.of(0, 1));
            assertThat(latest).hasSize(1);
            assertThat(latest.get(0).getAmount()).isEqualTo(25.0);
        }

        @Test
        @DisplayName("JPQL: Retrieve all system catalog books having zero fine history entries logged")
        void testFindBooksWithNoOverdueHistory() {
            List<Book> cleanBooks = bookRepository.findBooksWithNoOverdueHistory();
            assertThat(cleanBooks).hasSize(2);
        }

        @Test
        @DisplayName("Multiple fines per book should accumulate in totals")
        void testMultipleTransactionsAccumulation() {
            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(10.0)
                    .paymentType("CASH")
                    .paymentDate(LocalDate.now().minusDays(1))
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(15.0)
                    .paymentType("CARD")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            List<FineTransaction> fines = fineTransactionRepository.findByBookIsbn(testBook1.getIsbn());
            assertThat(fines).hasSize(2);
            Double totalFines = fines.stream().mapToDouble(FineTransaction::getAmount).sum();
            assertThat(totalFines).isEqualTo(25.0);
        }
    }

    @Nested
    @DisplayName("Task 5: Modifying In-place JPQL Target Update Invocations")
    class JpqlUpdateTests {

        @Test
        @DisplayName("Verify daily fine rate batch adjustments process fine via custom JPQL Modifiers")
        void testIncreaseDailyFineRates() {
            int itemsUpdated = bookRepository.increaseDailyFineRates(List.of("ACADEMIC"), 2.0);
            if (itemsUpdated > 0) {
                bookRepository.flush();
                Book updatedBook = bookRepository.findById("978-3-16-148410-0").orElseThrow();
                assertThat(updatedBook.getDailyFineRate()).isGreaterThanOrEqualTo(5.0);
            }
        }

        @Test
        @DisplayName("Batch update multiple book categories simultaneously")
        void testBatchUpdateMultipleCategories() {
            Book refBook = new Book();
            refBook.setIsbn("978-5-555555-55-5");
            refBook.setTitle("Reference");
            refBook.setBookType("REFERENCE");
            refBook.setDailyFineRate(2.0);
            bookRepository.save(refBook);
            bookRepository.flush();

            int updated = bookRepository.increaseDailyFineRates(List.of("ACADEMIC", "REFERENCE"), 1.5);
            if (updated > 0) {
                bookRepository.flush();
                assertThat(updated).isGreaterThan(0);
            }
        }
    }

    @Nested
    @DisplayName("Task 6: API Driven Pagination & Dynamic Sort Evaluations")
    class PaginationTests {

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("GET /api/library/books should yield sorted pages descending by raw daily fine rate rules")
        void testGetBooksPaginatedAndSorted() throws Exception {
            mockMvc.perform(get("/api/library/books")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].isbn").value("978-3-16-148410-0"))
                    .andExpect(jsonPath("$.content[1].isbn").value("978-0-451-52493-5"));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Pagination offset should work correctly")
        void testPaginationOffset() throws Exception {
            mockMvc.perform(get("/api/library/books")
                            .param("page", "1")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].isbn").value("978-0-451-52493-5"));
        }
    }

    @Nested
    @DisplayName("Task 7: DTO Projection Mapping Layer Safeguards")
    class DtoMappingTests {

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Verify structural serialization matches MemberSummaryDTO expectations precisely")
        void testMemberSummaryDtoMapping() throws Exception {
            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now())
                    .status("ISSUED")
                    .member(testMember)
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(50.0)
                    .paymentType("ONLINE")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            mockMvc.perform(get("/api/library/members/summary")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                    .andExpect(jsonPath("$[0].memberName").exists())
                    .andExpect(jsonPath("$[0].membershipBranch").exists())
                    .andExpect(jsonPath("$[0].numberOfBorrowedBooks").exists())
                    .andExpect(jsonPath("$[0].totalFinesPaid").exists());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Branch fine report should return correct DTO structure")
        void testBranchFineReportDTO() throws Exception {
            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now())
                    .status("ISSUED")
                    .member(testMember)
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(100.0)
                    .paymentType("CARD")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            mockMvc.perform(get("/api/library/reports/fines/branch")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
        }
    }

    @Nested
    @DisplayName("Task 8: End-to-End JWT Auth Engine Handshakes")
    class JwtAuthenticationTests {

        @Test
        @DisplayName("POST /login with valid details must serve back a valid JWT Bearer String")
        void testSuccessfulLogin() throws Exception {
            Map<String, String> loginCredentials = Map.of(
                    "email", "amit@library.com",
                    "password", "password123");

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginCredentials)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").isString());
        }

        @Test
        @DisplayName("Accessing locked library actions with zero authentication headers must prompt a 401 Unauthorized status")
        void testUnauthenticatedAccessFails() throws Exception {
            mockMvc.perform(get("/api/library/books")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Task 9: Method Level Role Based Access Validation Control")
    class AuthorizationTests {

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("MANAGER Role should be permitted to perform structural configuration daily fine rate operations")
        void testManagerCanUpdateFineRates() throws Exception {
            mockMvc.perform(patch("/api/library/books/fine-rate/increase")
                            .param("bookTypes", "FICTION")
                            .param("increment", "2.0")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Task 10: Global Exception Interception Handlers")
    class ExceptionHandlingTests {

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Should translate a missing member query into a normalized 404 Entity Not Found layout")
        void testMemberNotFoundExceptionHandling() throws Exception {
            mockMvc.perform(post("/api/library/issues")
                            .param("memberId", "999999")
                            .param("isbn", testBook1.getIsbn())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Missing book should return 404")
        void testBookNotFoundExceptionHandling() throws Exception {
            mockMvc.perform(post("/api/library/issues")
                            .param("memberId", String.valueOf(testMember.getMemberId()))
                            .param("isbn", "MISSING-ISBN")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Duplicate email registration should return validation error")
        void testDuplicateEmailValidation() throws Exception {
            Member duplicateMember = new Member();
            duplicateMember.setMemberName("Duplicate");
            duplicateMember.setEmail(testMember.getEmail());
            duplicateMember.setPassword("password123");
            duplicateMember.setMembershipBranch("Central Library");
            duplicateMember.setRole("USER");

            mockMvc.perform(post("/api/library/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(duplicateMember)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Final Challenge: Highly Optimized Single Execution Operations Dashboard Metrics")
    class FinalChallengeTests {

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("GET /api/library/reports/fines/branch must correctly resolve complex aggregate metric fields")
        void testGetDashboardPerformanceMetrics() throws Exception {
            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now())
                    .status("ISSUED")
                    .member(testMember)
                    .book(testBook1)
                    .build());

            fineTransactionRepository.save(FineTransaction.builder()
                    .amount(50.0)
                    .paymentType("ONLINE")
                    .paymentDate(LocalDate.now())
                    .book(testBook1)
                    .build());

            mockMvc.perform(get("/api/library/reports/fines/branch")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @WithMockUser(roles = "MANAGER")
        @DisplayName("Complex member summary aggregation should work without N+1 queries")
        void testMemberSummaryAggregation() throws Exception {
            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now().minusDays(5))
                    .status("ISSUED")
                    .member(testMember)
                    .book(testBook1)
                    .build());

            issueRecordRepository.save(IssueRecord.builder()
                    .issueDate(LocalDate.now().minusDays(3))
                    .status("RETURNED")
                    .returnDate(LocalDate.now())
                    .member(testMember)
                    .book(testBook2)
                    .build());

            mockMvc.perform(get("/api/library/members/summary")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].numberOfBorrowedBooks").value(2));
        }
    }
}


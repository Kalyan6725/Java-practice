# Banking API - TDD Test Suite Structure

## ✅ Completed Test Classes

### Service Layer (Unit Tests - Mockito)
1. **CustomerServiceTest** ✅
   - Positive: Create, Read, Update, Delete, Find by Email
   - Negative: Duplicate email, Not found, Invalid input
   - AAA Pattern: All methods follow Arrange-Act-Assert

2. **AccountServiceTest** ✅
   - Positive: Create, Deposit, Withdraw, Transfer operations
   - Negative: Invalid customer, Negative balance, Duplicate account
   - Edge cases: Zero amounts, Insufficient balance

3. **TransactionServiceTest** ✅
   - Positive: Transfer between accounts, Query transactions
   - Negative: Same account transfer, Not found accounts
   - Business rules: Amount validation, Balance verification

### Repository Layer (Integration Tests - @SpringBootTest + @Transactional)
1. **CustomerRepositoryTest** ✅
   - CRUD operations with H2 in-memory database
   - Unique constraint validation
   - Query methods: findByEmail, existsByEmail

### Exception Handling
1. **GlobalExceptionHandlerTest** ✅ (partially updated for banking domain)
   - Maps exceptions to HTTP status codes
   - CustomerNotFoundException → 404
   - AccountNotFoundException → 404
   - DuplicateEmailException → 409
   - InsufficientBalanceException → 400

---

## 📋 Remaining Test Classes to Create

### Repository Layer
**AccountRepositoryTest**
```java
@SpringBootTest
@Transactional
class AccountRepositoryTest {
    // Test CRUD, unique constraints, findByCustomerId
    // Similar structure to CustomerRepositoryTest
}
```

### Controller Layer (MockMvc)
**CustomerControllerTest**
- Test REST endpoints: POST, GET, PUT, DELETE
- Mock MerchantService, verify JSON responses
- Use MockMvcBuilders.standaloneSetup()
- Verify status codes and response structure

**AccountControllerTest**
- Test account endpoints
- Mock AccountService
- Verify deposit/withdraw/transfer flows

**TransactionControllerTest**
- Test transaction queries
- Mock TransactionService
- Verify transaction list responses

### Security & Integration
**SecurityTest**
- Test JWT authentication
- Public endpoints: /api/auth/register, /api/auth/login
- Protected endpoints: require Bearer token
- Test expired/invalid tokens

**CustomerIntegrationTest**
- End-to-end: Register → Login → Create Account → Deposit → Withdraw
- Uses @WebAppConfiguration + springSecurity()
- Extracts JWT tokens and uses them in subsequent calls

**TransferIntegrationTest**
- Full transfer workflow test
- Account creation → Transfer → Verify balances
- Verify both transaction records created
- Test atomic transaction handling

---

## 🎯 Test Pattern Template (AAA - Arrange-Act-Assert)

```java
@Test
void methodName_whenCondition_expectedOutcome() {
    // ARRANGE: Set up test data and mocks
    when(mock.method()).thenReturn(value);
    
    // ACT: Execute the method under test
    Object result = service.method();
    
    // ASSERT: Verify results
    assertEquals(expected, actual);
    verify(mock).method();
}
```

---

## 📊 Test Coverage by Layer

| Layer | Unit Tests | Integration | Controller | Total |
|-------|-----------|-------------|-----------|-------|
| Service | 3 | - | - | 3 |
| Repository | - | 1 | - | 1 |
| Controller | - | - | 3 | 3 |
| Security | - | 2 | - | 2 |
| Exception | 1 | - | - | 1 |
| **TOTAL** | **4** | **3** | **3** | **12** |

---

## 🚀 Next Steps

1. Create `AccountRepositoryTest` - mirror of `CustomerRepositoryTest`
2. Create controller tests using MockMvc pattern
3. Create `SecurityTest` with JWT token handling
4. Create integration tests with full flow testing
5. Run full Maven test suite: `mvn clean test`

---

## 📌 Key Testing Principles Applied

✅ **AAA Pattern**: Every test follows Arrange-Act-Assert structure
✅ **Descriptive Names**: Test names clearly describe scenario and expected outcome
✅ **Positive & Negative Cases**: Tests verify both success and failure paths
✅ **Edge Cases**: Zero amounts, negative values, boundary conditions
✅ **No Test Duplication**: Shared test data factory and fixtures
✅ **Maintainability**: Clear assertions with meaningful error messages
✅ **TDD First**: Tests define behavior before implementation

---

## 🔧 Execution

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=CustomerServiceTest
```

Run tests matching pattern:
```bash
mvn test -Dtest=*ServiceTest
```

View coverage:
```bash
mvn test jacoco:report
```

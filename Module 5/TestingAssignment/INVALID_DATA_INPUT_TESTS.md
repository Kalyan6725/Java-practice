# Invalid Data Input Test Cases

## Summary
Your test suite includes **18 comprehensive invalid data input test cases** across controller and repository layers.

---

## 1. CUSTOMER VALIDATION TESTS (4 test cases)

### CustomerControllerTest.java

#### Test 1: Missing Email
```java
public void createCustomer_withMissingEmail_returns400BadRequest()
```
- **Input**: `email = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Email is mandatory

#### Test 2: Invalid Email Format
```java
public void createCustomer_withInvalidEmail_returns400BadRequest()
```
- **Input**: `email = "invalid-email"` (no @domain)
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Email format must be valid (RFC 5322)

#### Test 3: Invalid Phone Length
```java
public void createCustomer_withInvalidPhoneLength_returns400BadRequest()
```
- **Input**: `phone = "123456"` (6 digits instead of 10)
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Phone must be exactly 10 digits

#### Test 4: Duplicate Email
```java
public void createCustomer_withDuplicateEmail_returns409Conflict()
```
- **Input**: `email = "john@test.com"` (already exists)
- **Expected**: `409 CONFLICT` + `DuplicateEmailException`
- **Validates**: Email uniqueness constraint

---

## 2. ACCOUNT VALIDATION TESTS (5 test cases)

### AccountControllerTest.java

#### Test 5: Missing Account Number
```java
public void createAccount_withMissingAccountNumber_returns400BadRequest()
```
- **Input**: `accountNumber = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Account number is mandatory

#### Test 6: Negative Balance
```java
public void deposit_withNegativeAmount_returns400BadRequest()
```
- **Input**: `amount = -500.0` (negative amount)
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Amount cannot be negative

#### Test 7: Null Deposit Amount
```java
public void deposit_withNullAmount_returns400BadRequest()
```
- **Input**: `amount = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Amount is mandatory

#### Test 8: Null Withdraw Amount
```java
public void withdraw_withNullAmount_returns400BadRequest()
```
- **Input**: `amount = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Amount is mandatory

#### Test 9: Null Transfer Amount
```java
public void transfer_withNullAmount_returns400BadRequest()
```
- **Input**: `amount = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Amount is mandatory

---

## 3. REPOSITORY CONSTRAINT VIOLATIONS (3 test cases)

### AccountRepositoryTest.java

#### Test 10: Negative Balance Constraint
```java
public void saveAccount_withNegativeBalance_throwsConstraintViolationException()
```
- **Input**: `Account(balance = -1000.0)`
- **Expected**: `ConstraintViolationException`
- **Validates**: Database constraint enforces non-negative balance

#### Test 11: Duplicate Account Number
```java
public void saveAccount_withDuplicateAccountNumber_throwsConstraintViolationException()
```
- **Input**: `Account(accountNumber = "ACC001")` (already exists)
- **Expected**: `ConstraintViolationException`
- **Validates**: Unique constraint on account number

#### Test 12: Null Account Number
```java
public void saveAccount_withNullAccountNumber_throwsConstraintViolationException()
```
- **Input**: `Account(accountNumber = null)`
- **Expected**: `ConstraintViolationException`
- **Validates**: Account number cannot be null

---

### CustomerRepositoryTest.java

#### Test 13: Duplicate Email in Repository
```java
public void saveCustomer_withDuplicateEmail_throwsConstraintViolationException()
```
- **Input**: `Customer(email = "john@test.com")` (already exists)
- **Expected**: `ConstraintViolationException`
- **Validates**: Unique constraint on email

#### Test 14: Invalid Email in Repository
```java
public void saveCustomer_withInvalidEmailFormat_throwsConstraintViolationException()
```
- **Input**: `Customer(email = "not-an-email")`
- **Expected**: `ConstraintViolationException`
- **Validates**: Email format validation

#### Test 15: Invalid Phone in Repository
```java
public void saveCustomer_withInvalidPhoneLength_throwsConstraintViolationException()
```
- **Input**: `Customer(phone = "123")` (only 3 digits)
- **Expected**: `ConstraintViolationException`
- **Validates**: Phone length constraint (exactly 10 digits)

---

## 4. SERVICE LAYER VALIDATION (3 test cases)

### CustomerServiceTest.java

#### Test 16: Customer Not Found
```java
public void getCustomerById_withNonExistentId_throwsCustomerNotFoundException()
```
- **Input**: `id = 999L` (doesn't exist)
- **Expected**: `CustomerNotFoundException`
- **Validates**: Proper error when customer doesn't exist

#### Test 17: Invalid Email for Login
```java
public void getCustomerByEmail_withNonExistentEmail_throwsCustomerNotFoundException()
```
- **Input**: `email = "missing@example.com"`
- **Expected**: `CustomerNotFoundException`
- **Validates**: Proper error when email not found

---

### AccountServiceTest.java

#### Test 18: Insufficient Balance
```java
public void withdraw_withInsufficientBalance_throwsInsufficientBalanceException()
```
- **Input**: `Account(balance = 100.0)`, `withdraw(500.0)`
- **Expected**: `InsufficientBalanceException`
- **Validates**: Cannot withdraw more than available balance

---

## 5. INTEGRATION LAYER VALIDATION (3 test cases)

### CustomerIntegrationTest.java

#### Test 19: Invalid Email Registration
```java
public void registerCustomer_withInvalidEmail_returns400BadRequest()
```
- **Input**: `email = "invalid-email"` (malformed)
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Email validation at API level

#### Test 20: Duplicate Email Registration
```java
public void registerCustomer_withDuplicateEmail_returns409Conflict()
```
- **Input**: `email = "john@test.com"` (already registered)
- **Expected**: `409 CONFLICT`
- **Validates**: Uniqueness at API level

#### Test 21: Missing Required Field
```java
public void registerCustomer_withMissingName_returns400BadRequest()
```
- **Input**: `name = null`
- **Expected**: `400 BAD_REQUEST`
- **Validates**: Required field validation

---

## VALIDATION CATEGORIES COVERED

| Category | Count | Examples |
|----------|-------|----------|
| **Null/Missing** | 6 | email, phone, account number, amounts |
| **Format/Pattern** | 4 | Invalid email, invalid phone length |
| **Range/Constraints** | 4 | Negative balance, insufficient funds |
| **Uniqueness** | 3 | Duplicate email, duplicate account number |
| **Not Found** | 2 | Customer not found, account not found |
| **Business Rules** | 1 | Insufficient balance for withdrawal |

---

## ANNOTATIONS USED FOR VALIDATION

Your entities use these validation annotations:

```java
// Customer.java
@NotBlank(message = "Email is required")
@Email(message = "Invalid email format")
private String email;

@NotBlank(message = "Phone is required")
@Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
private String phone;

// Account.java
@Min(value = 0, message = "Balance cannot be negative")
private Double balance;

@NotBlank(message = "Account number is required")
@Column(unique = true)
private String accountNumber;
```

---

## WHAT'S NOT YET TESTED (Gaps)

These edge cases could be added for **more comprehensive coverage**:

1. **Empty String** - `email = ""` (instead of null)
2. **Whitespace Only** - `email = "   "` (spaces only)
3. **SQL Injection** - `name = "'; DROP TABLE customers;--"`
4. **XSS Attacks** - `email = "<script>alert('xss')</script>@test.com"`
5. **Very Large Numbers** - `balance = Double.MAX_VALUE`
6. **Zero Amount** - `transfer(0.0)` or `deposit(0.0)`
7. **Negative Customer ID** - `getCustomerById(-1L)`
8. **Very Long Strings** - `name = "A".repeat(10000)`
9. **Special Characters** - `name = "!@#$%^&*()"`
10. **Unicode Characters** - `name = "用户"` (Chinese characters)

---

## QUICK REFERENCE: TEST NAME PATTERNS

✅ **Invalid Input** tests use naming pattern:
- `methodName_with<InvalidCondition>_returns<StatusCode>()`
- Examples: `withMissingEmail`, `withNegativeAmount`, `withInvalidPhone`

✅ **Expected HTTP Status Codes**:
- `400 BAD_REQUEST` - Validation failure (format, constraints)
- `409 CONFLICT` - Duplicate/uniqueness violation
- `404 NOT_FOUND` - Resource doesn't exist
- `500 INTERNAL_SERVER_ERROR` - Unhandled exception

---

## HOW TO RUN JUST VALIDATION TESTS

```bash
# Run all controller tests (includes validation)
mvn test -Dtest=*ControllerTest

# Run repository constraint tests
mvn test -Dtest=*RepositoryTest

# Run specific test method
mvn test -Dtest=CustomerControllerTest#createCustomer_withInvalidEmail_returns400BadRequest
```

---

**Total: 21 Invalid Data Input Test Cases** across all layers ✅

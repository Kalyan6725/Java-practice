# Spring Boot Fulfillment System - Testing Assignment

## Project Overview
A complete Spring Boot REST API application for managing merchant fulfillment operations including order processing, shipments, and merchant management with JWT authentication.

## ✅ Completion Status

### Architecture & Components
- ✅ **Entities** (3): Merchant, Order, Shipment with JPA annotations
- ✅ **DTOs** (6): RegisterRequest, LoginRequest, MerchantRequest/Response, OrderRequest/Response, AuthResponse
- ✅ **Repositories** (3): JpaRepository implementations for all entities
- ✅ **Services** (6): Interfaces + implementations for Merchant, Order, Shipment operations
- ✅ **Controllers** (4): AuthController, MerchantController, OrderController, ShipmentController
- ✅ **Security**: JWT authentication with JwtService, JwtFilter, SecurityConfig, CustomUserDetailsService
- ✅ **Exception Handling**: Custom exceptions (MerchantNotFoundException, OrderNotFoundException, DuplicateEmailException) with GlobalExceptionHandler

### Testing Coverage
- ✅ **25 Comprehensive Tests** - All passing
- ✅ **MerchantServiceTest** (10 tests): CRUD operations, password encryption, duplicate email validation
- ✅ **OrderServiceTest** (15 tests): Order creation, processing, cancellation, shipment splitting, validations

### API Endpoints
```
Authentication:
  POST   /api/auth/register         - Register new merchant
  POST   /api/auth/login            - Login merchant

Merchants:
  POST   /api/merchants             - Create merchant (201)
  GET    /api/merchants             - List all merchants
  GET    /api/merchants/{id}        - Get merchant by ID
  PUT    /api/merchants/{id}        - Update merchant
  DELETE /api/merchants/{id}        - Delete merchant

Orders:
  POST   /api/orders                - Create order
  GET    /api/orders                - List all orders
  GET    /api/orders/{id}           - Get order by ID
  PUT    /api/orders/{id}           - Update order
  DELETE /api/orders/{id}           - Delete order
  POST   /api/orders/process        - Process order (PENDING → PROCESSED)
  POST   /api/orders/cancel         - Cancel order
  POST   /api/orders/split-shipment - Split shipment between orders

Shipments:
  GET    /api/shipments             - List all shipments
  GET    /api/shipments/{id}        - Get shipment by ID
  GET    /api/orders/{id}/shipments - Get shipments for order
```

### Business Rules Implemented ✅

**Merchant Management:**
- ✅ Email must be unique (409 Conflict on duplicate)
- ✅ Name is mandatory
- ✅ Email format validation
- ✅ Tax ID: exactly 10 alphanumeric characters
- ✅ Password encryption using BCryptPasswordEncoder
- ✅ Passwords not exposed in response DTOs

**Order Processing:**
- ✅ Order total amount cannot be negative (400 Bad Request)
- ✅ Order number must be unique
- ✅ Merchant must exist before creating order (404 if missing)
- ✅ Order status: PENDING → PROCESSED → CANCELLED
- ✅ Process Order: Automatically creates shipment record
- ✅ Cancel Order: Only PENDING orders can be cancelled (400 if PROCESSED)
- ✅ Split Shipment: Creates two shipment records atomically

**Security:**
- ✅ /login and /register are public endpoints
- ✅ All other endpoints require JWT authentication
- ✅ Invalid JWT returns 401 Unauthorized
- ✅ Expired JWT returns 401 Unauthorized
- ✅ Missing Authorization header returns 401 Unauthorized

### HTTP Status Codes
- ✅ 200 OK - Successful GET/PUT requests
- ✅ 201 Created - POST requests
- ✅ 204 No Content - DELETE requests
- ✅ 400 Bad Request - Validation errors, business rule violations
- ✅ 401 Unauthorized - Missing/invalid JWT
- ✅ 404 Not Found - Merchant/Order/Shipment not found
- ✅ 409 Conflict - Duplicate email

### Stack
- **Framework**: Spring Boot 4.1.0
- **Language**: Java 21
- **Database**: H2 (in-memory for testing)
- **Persistence**: Spring Data JPA with Hibernate
- **Security**: Spring Security + JWT (JJWT 0.12.6)
- **Validation**: Jakarta Validation API
- **ORM**: Hibernate 7.4.1
- **Build**: Maven 3.9.16
- **Testing**: JUnit 5 + Mockito 5.23.0
- **Lombok**: For reducing boilerplate code

### File Structure
```
src/main/java/org/northernarc/testingassignment/
├── entity/
│   ├── Merchant.java
│   ├── Order.java
│   └── Shipment.java
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── MerchantRequest.java
│   ├── MerchantResponse.java
│   ├── OrderRequest.java
│   ├── OrderResponse.java
│   └── AuthResponse.java
├── repository/
│   ├── MerchantRepository.java
│   ├── OrderRepository.java
│   └── ShipmentRepository.java
├── service/
│   ├── MerchantService.java (Interface)
│   ├── OrderService.java (Interface)
│   ├── ShipmentService.java (Interface)
│   └── impl/
│       ├── MerchantServiceImpl.java
│       ├── OrderServiceImpl.java
│       └── ShipmentServiceImpl.java
├── controller/
│   ├── AuthController.java
│   ├── MerchantController.java
│   ├── OrderController.java
│   └── ShipmentController.java
├── security/
│   ├── JwtService.java
│   ├── JwtFilter.java
│   ├── CustomUserDetailsService.java
│   └── SecurityConfig.java
├── exception/
│   ├── MerchantNotFoundException.java
│   ├── OrderNotFoundException.java
│   ├── DuplicateEmailException.java
│   └── GlobalExceptionHandler.java
└── TestingAssignmentApplication.java

src/test/java/org/northernarc/testingassignment/
├── service/
│   ├── MerchantServiceTest.java (10 tests)
│   └── OrderServiceTest.java (15 tests)
└── TestingAssignmentApplicationTests.java
```

### Build & Run
```bash
# Compile
mvn clean compile

# Run Tests
mvn test

# Build JAR
mvn clean package

# Run Application
java -jar target/TestingAssignment-0.0.1-SNAPSHOT.jar
```

### Test Results
```
Tests run: 25
Failures: 0
Errors: 0
Skipped: 0
Time: 24.767 seconds
Status: ✅ BUILD SUCCESS
```

### Key Design Patterns
1. **Dependency Inversion**: Service interfaces with implementations
2. **DTO Pattern**: Request/Response objects separate from entities
3. **Exception Translation**: Custom exceptions with appropriate HTTP status codes
4. **Transaction Management**: @Transactional for atomic operations (split shipment)
5. **Security**: JWT-based stateless authentication
6. **Validation**: Bean validation on DTOs and entities
7. **Repository Pattern**: Data access abstraction via JPA repositories

### Minimal Implementation Philosophy
- ✅ No unnecessary features
- ✅ All code exists to satisfy test requirements
- ✅ Clean, readable, maintainable code
- ✅ Proper error handling with meaningful messages
- ✅ DRY principle applied throughout

## Running Tests in IDE

### IntelliJ IDEA
1. Open project in IntelliJ
2. Right-click on `src/test/java` → Run Tests
3. Or use `Ctrl+Shift+F10` on test files

### Command Line
```bash
mvn test                           # Run all tests
mvn test -Dtest=MerchantServiceTest   # Run specific test class
mvn test -Dtest=OrderServiceTest#testCreateOrderWithValidData   # Run specific test
```

## Database Schema
The application uses H2 in-memory database with Hibernate auto-DDL (create-drop for testing).

### Tables
- **merchants**: id, name, email (unique), password, tax_id
- **orders**: id, order_number (unique), merchant_id, total_amount, order_status
- **shipments**: id, tracking_number (unique), order_id, carrier, shipping_cost, shipment_date

## Notes
- Default profile uses H2 in-memory database for rapid testing
- JWT secret key is in JwtService (128+ characters for HS256)
- Token expiration: 10 hours
- BCrypt strength: default (10 rounds)
- All endpoints except /auth/register and /auth/login require JWT in Authorization header
- Format: `Authorization: Bearer <token>`

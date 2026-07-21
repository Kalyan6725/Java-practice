# Postman Testing Guide — Loan Management API

**Base URL:** `http://localhost:8080`

## Conventions
- Every request (except register/login/verify) needs header:
  `Authorization: Bearer <TOKEN>`
- Every request with a body needs header: `Content-Type: application/json`
- Token column values: `USER`, `UW` (underwriter), `MANAGER`, `ADMIN`, or `None`.
- Save tokens after login and reuse them.

## Seeded logins
| Role | Email | Password |
| ----------- | ----------------------- | --------------- |
| ADMIN | admin@example.com | admin@123 |
| MANAGER | manager@example.com | manager@123 |
| UNDERWRITER | underwriter@example.com | underwriter@123 |
| USER | (self-register below) | password123 |

> Tip: On each login request add a **Tests** script to auto-save the token:
> ```javascript
> pm.environment.set("ADMIN_TOKEN", pm.response.json().data.token);
> ```
> Then use `Authorization: Bearer {{ADMIN_TOKEN}}`.

---

## 1. Authentication (`/api/auth`)

### 1.1 Register a USER
- **Method:** `POST`
- **Endpoint:** `/api/auth/register`
- **Token:** None
- **Body:**
```json
{
  "customerName": "Rahul Sharma",
  "email": "rahul.sharma@example.com",
  "password": "password123",
  "phone": "+919812345678",
  "address": "12 MG Road, Hyderabad",
  "branch": "Hyderabad"
}
```
- **Expect:** `201 Created`

### 1.2 Login (any role)
- **Method:** `POST`
- **Endpoint:** `/api/auth/login`
- **Token:** None
- **Body:**
```json
{ "email": "admin@example.com", "password": "admin@123" }
```
- **Expect:** `200 OK`, JWT in `data.token`.

### 1.3 Verify token
- **Method:** `GET`
- **Endpoint:** `/api/auth/verify`
- **Token:** any (sent in `Authorization` header)
- **Body:** none
- **Expect:** `200 OK`

### 1.4 Current profile
- **Method:** `GET`
- **Endpoint:** `/api/auth/me`
- **Token:** any
- **Body:** none
- **Expect:** `200 OK`

---

## 2. Loan Products (`/api/loan-products`)

### 2.1 Create product
- **Method:** `POST`
- **Endpoint:** `/api/loan-products`
- **Token:** ADMIN
- **Body:**
```json
{
  "loanCode": "LP001",
  "loanName": "Personal Prime",
  "loanType": "PERSONAL",
  "minimumAmount": 10000,
  "maximumAmount": 1000000,
  "interestRate": 12.0,
  "minimumTenure": 12,
  "maximumTenure": 60,
  "processingFee": 500,
  "dailyPenaltyRate": 2.5,
  "active": true
}
```
- **Expect:** `201 Created` (USER/MANAGER → `403`)
- `loanType` ∈ `PERSONAL | HOME | VEHICLE | EDUCATION | BUSINESS`

### 2.2 Get all products
- **Method:** `GET` · **Endpoint:** `/api/loan-products` · **Token:** any · **Expect:** `200`

### 2.3 Get active products
- **Method:** `GET` · **Endpoint:** `/api/loan-products/active` · **Token:** any · **Expect:** `200`

### 2.4 Get one product
- **Method:** `GET` · **Endpoint:** `/api/loan-products/LP001` · **Token:** any · **Expect:** `200`

### 2.5 Paged products
- **Method:** `GET`
- **Endpoint:** `/api/loan-products/paged?page=0&size=10&sortBy=dailyPenaltyRate&direction=DESC`
- **Token:** any · **Expect:** `200`

### 2.6 Search products
- **Method:** `GET`
- **Endpoint:** `/api/loan-products/search?loanType=PERSONAL&page=0&size=10`
- Optional params: `loanName`, `minPenalty`, `maxPenalty`, `sortBy`, `direction`
- **Token:** any · **Expect:** `200`

### 2.7 Update product
- **Method:** `PUT`
- **Endpoint:** `/api/loan-products`
- **Token:** MANAGER or ADMIN
- **Body:** same shape as 2.1 (with existing `loanCode`)
- **Expect:** `200` (USER → `403`)

### 2.8 Delete product
- **Method:** `DELETE`
- **Endpoint:** `/api/loan-products/LP001`
- **Token:** ADMIN · **Expect:** `200` (USER/MANAGER → `403`)

---

## 3. Loan Applications (`/api/loan-applications`)

### 3.1 Apply for a loan
- **Method:** `POST`
- **Endpoint:** `/api/loan-applications`
- **Token:** USER (also MANAGER/ADMIN)
- **Body:**
```json
{ "loanCode": "LP001", "requestedAmount": 240000, "tenureMonths": 24 }
```
- **Expect:** `201 Created`, status `SUBMITTED`. Save `data.applicationId`.
- Over-max amount (e.g. `5000000`) → `400 VALIDATION_ERROR`.

### 3.2 My applications
- **Method:** `GET` · **Endpoint:** `/api/loan-applications/my` · **Token:** any · **Expect:** `200`

### 3.3 Get one application
- **Method:** `GET` · **Endpoint:** `/api/loan-applications/{applicationId}` · **Token:** owner USER or staff · **Expect:** `200` (non-owner USER → `403`)

### 3.4 All applications (optional `?status=`)
- **Method:** `GET`
- **Endpoint:** `/api/loan-applications` or `/api/loan-applications?status=SUBMITTED`
- **Token:** UW / MANAGER / ADMIN · **Expect:** `200` (USER → `403`)

### 3.5 Pending review queue
- **Method:** `GET` · **Endpoint:** `/api/loan-applications/pending` · **Token:** UW / MANAGER / ADMIN · **Expect:** `200` (USER → `403`)

### 3.6 Applications by customer
- **Method:** `GET` · **Endpoint:** `/api/loan-applications/customer/{customerId}` · **Token:** UW / MANAGER / ADMIN · **Expect:** `200`

### 3.7 Review (approve / reject)
- **Method:** `PUT`
- **Endpoint:** `/api/loan-applications/{applicationId}/review`
- **Token:** UNDERWRITER or ADMIN
- **Body (approve):**
```json
{ "decision": "APPROVED", "remarks": "Verified, approved", "approvedAmount": 240000 }
```
- **Body (reject):**
```json
{ "decision": "REJECTED", "remarks": "Insufficient income" }
```
- `decision` ∈ `UNDER_REVIEW | APPROVED | REJECTED`
- **Expect:** `200`. On APPROVED, a loan account is auto-created.

---

## 4. Loan Accounts (`/api/loan-accounts`)

### 4.1 My accounts
- **Method:** `GET` · **Endpoint:** `/api/loan-accounts/my` · **Token:** any · **Expect:** `200`

### 4.2 Get one account
- **Method:** `GET` · **Endpoint:** `/api/loan-accounts/{loanAccountId}` · **Token:** owner USER or staff · **Expect:** `200`

### 4.3 All accounts
- **Method:** `GET` · **Endpoint:** `/api/loan-accounts` · **Token:** MANAGER / ADMIN · **Expect:** `200` (USER → `403`). Grab `loanAccountId`.

### 4.4 Accounts by customer
- **Method:** `GET` · **Endpoint:** `/api/loan-accounts/customer/{customerId}` · **Token:** self USER or staff · **Expect:** `200`

### 4.5 Accounts by status
- **Method:** `GET` · **Endpoint:** `/api/loan-accounts/status/ACTIVE` · **Token:** MANAGER / ADMIN · **Expect:** `200`
- status ∈ `APPROVED | DISBURSED | ACTIVE | CLOSED | OVERDUE`

### 4.6 Disburse a loan
- **Method:** `POST`
- **Endpoint:** `/api/loan-accounts/{loanAccountId}/disburse`
- **Token:** MANAGER or ADMIN
- **Body:** none
- **Expect:** `200`, status → `ACTIVE` (USER → `403`)

---

## 5. EMI Payments (`/api/emi-payments`)

### 5.1 Pay an EMI
- **Method:** `POST`
- **Endpoint:** `/api/emi-payments`
- **Token:** USER (owner) / MANAGER / ADMIN
- **Body:**
```json
{ "loanAccountId": 1, "paymentType": "UPI" }
```
- Optional fields: `installmentNo` (defaults to next unpaid), `amount` (defaults to scheduled EMI + penalty)
- `paymentType` ∈ `CASH | CARD | ONLINE | UPI`
- **Expect:** `201 Created`, status `PAID`

### 5.2 Get one payment
- **Method:** `GET` · **Endpoint:** `/api/emi-payments/{paymentId}` · **Token:** owner or staff · **Expect:** `200`

### 5.3 All payments
- **Method:** `GET` · **Endpoint:** `/api/emi-payments` · **Token:** MANAGER / ADMIN · **Expect:** `200` (USER → `403`)

### 5.4 Payments by account
- **Method:** `GET` · **Endpoint:** `/api/emi-payments/account/{loanAccountId}` · **Token:** owner or staff · **Expect:** `200`

### 5.5 EMI schedule (amortization)
- **Method:** `GET` · **Endpoint:** `/api/emi-payments/account/{loanAccountId}/schedule` · **Token:** owner or staff · **Expect:** `200`, one row per installment (PAID / PENDING / OVERDUE)

---

## 6. Customers (`/api/customers`)

### 6.1 Create customer (admin)
- **Method:** `POST`
- **Endpoint:** `/api/customers`
- **Token:** ADMIN
- **Body:**
```json
{
  "customerName": "Anita Desai",
  "email": "anita.desai@example.com",
  "password": "secret123",
  "branch": "Pune",
  "phone": "+919800000000",
  "address": "45 FC Road, Pune",
  "role": "UNDERWRITER",
  "status": "ACTIVE"
}
```
- `role` ∈ `USER | UNDERWRITER | MANAGER | ADMIN` · **Expect:** `201`

### 6.2 Get all customers
- **Method:** `GET` · **Endpoint:** `/api/customers` · **Token:** MANAGER / ADMIN · **Expect:** `200` (USER → `403`)

### 6.3 Get one customer
- **Method:** `GET` · **Endpoint:** `/api/customers/{customerId}` · **Token:** self USER or staff · **Expect:** `200` (other id as USER → `403`; missing id → `404`)

### 6.4 My profile
- **Method:** `GET` · **Endpoint:** `/api/customers/me` · **Token:** any · **Expect:** `200`

### 6.5 Update customer (admin)
- **Method:** `PUT`
- **Endpoint:** `/api/customers`
- **Token:** ADMIN
- **Body:**
```json
{
  "customerId": 1,
  "customerName": "Rahul S.",
  "phone": "+919812345000",
  "address": "New Address, Hyderabad",
  "branch": "Hyderabad",
  "role": "USER",
  "status": "ACTIVE"
}
```
- **Expect:** `200`. `password` optional (blank → unchanged). `email` not updatable.

### 6.6 Update my profile
- **Method:** `PUT`
- **Endpoint:** `/api/customers/me`
- **Token:** any (USER updates self)
- **Body:**
```json
{
  "customerId": 1,
  "customerName": "Rahul Sharma",
  "phone": "+919812345678",
  "address": "12 MG Road, Hyderabad",
  "branch": "Hyderabad"
}
```
- **Expect:** `200` (role/status ignored for non-admin)

### 6.7 Delete customer
- **Method:** `DELETE` · **Endpoint:** `/api/customers/{customerId}` · **Token:** ADMIN · **Expect:** `200` (USER → `403`)

### 6.8 All customer summaries
- **Method:** `GET` · **Endpoint:** `/api/customers/summaries` · **Token:** MANAGER / ADMIN · **Expect:** `200`

### 6.9 One customer summary
- **Method:** `GET` · **Endpoint:** `/api/customers/{customerId}/summary` · **Token:** self USER or staff · **Expect:** `200`

### 6.10 Summaries by branch
- **Method:** `GET` · **Endpoint:** `/api/customers/branch/Hyderabad/summaries` · **Token:** MANAGER / ADMIN · **Expect:** `200`

### 6.11 Summaries with min loans
- **Method:** `GET` · **Endpoint:** `/api/customers/summaries/min-loans?minLoans=1` · **Token:** MANAGER / ADMIN · **Expect:** `200`

---

## 7. Dashboard (`/api/dashboard`)

### 7.1 Portfolio metrics
- **Method:** `GET`
- **Endpoint:** `/api/dashboard`
- **Token:** MANAGER or ADMIN
- **Body:** none
- **Expect:** `200` (USER → `403`)

---

## 8. Negative / security checks
| # | Request | Token | Expect |
| - | ------------------------------------------- | ----- | ------ |
| 1 | `GET /api/loan-products` | None | `401` |
| 2 | `POST /api/auth/login` (wrong password) | None | `401` |
| 3 | `POST /api/loan-products` | USER | `403` |
| 4 | `DELETE /api/loan-products/LP001` | USER | `403` |
| 5 | `GET /api/loan-applications/pending` | USER | `403` |
| 6 | `POST /api/loan-accounts/{id}/disburse` | USER | `403` |
| 7 | `GET /api/dashboard` | USER | `403` |
| 8 | `GET /api/customers/999999` | ADMIN | `404` |
| 9 | `POST /api/loan-applications` (amount>max) | USER | `400` |

---

## Suggested end-to-end flow
1. Register USER → Login USER, ADMIN, UNDERWRITER, MANAGER (save 4 tokens)
2. ADMIN creates product `LP001`
3. USER applies → save `applicationId`
4. UNDERWRITER approves → loan account auto-created
5. MANAGER lists accounts → save `loanAccountId`
6. MANAGER disburses the account (→ ACTIVE)
7. USER pays EMI → view schedule
8. MANAGER views dashboard
9. Run the negative checks in section 8

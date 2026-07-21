# Default Roles & Credentials

Base URL: `http://localhost:8080`
Login endpoint: `POST /api/auth/login`

> ⚠️ These are default/seed credentials for local development and testing only.
> **Change them immediately in production.**

## Seeded accounts (created automatically on startup)

| Role | Email | Password | Branch |
| ------------ | -------------------------- | ---------------- | ------------- |
| ADMIN | admin@example.com | admin@123 | Head Office |
| MANAGER | manager@example.com | manager@123 | Mumbai Branch |
| UNDERWRITER | underwriter@example.com | underwriter@123 | Mumbai Branch |

## USER account (self-registered)

There is no seeded USER. Create one via `POST /api/auth/register` (role is always
forced to `USER`):

```json
{
  "customerName": "Samineni Kalyan",
  "email": "kalyan@gmail.com",
  "password": "Kalyan@6725",
  "phone": "9381006725",
  "address": "Velachery, Chennai",
  "branch": "Chennai"
}
```

| Role | Email | Password |
| ---- | ------------------------- | ----------- |
| USER | kalyan@gmail.com | Kalyan@6725 |

## How to log in (Postman)

**Method:** `POST`
**Endpoint:** `http://localhost:8080/api/auth/login`
**Headers:** `Content-Type: application/json`
**Body:**

```json
{ "email": "admin@example.com", "password": "admin@123" }
```

Response returns a JWT in `data.token`. Use it on protected routes as:

```
Authorization: Bearer <token>
```

## Role capabilities (summary)

| Capability | USER | UNDERWRITER | MANAGER | ADMIN |
| ------------------------------ | :--: | :---------: | :-----: | :---: |
| Register / Login | ✅ | ✅ | ✅ | ✅ |
| Browse loan products | ✅ | ✅ | ✅ | ✅ |
| Create / Delete loan product | ❌ | ❌ | ❌ | ✅ |
| Update loan product | ❌ | ❌ | ✅ | ✅ |
| Apply for a loan | ✅ | ❌ | ✅ | ✅ |
| Review (approve/reject) apps | ❌ | ✅ | ❌ | ✅ |
| List all applications / queue | ❌ | ✅ | ✅ | ✅ |
| Disburse a loan account | ❌ | ❌ | ✅ | ✅ |
| Pay EMI (own loan) | ✅ | ❌ | ✅ | ✅ |
| View all accounts / payments | ❌ | ❌ | ✅ | ✅ |
| Manage customers (CRUD) | ❌ | ❌ | ❌ | ✅ |
| View dashboard | ❌ | ❌ | ✅ | ✅ |

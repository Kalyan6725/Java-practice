# Northern Arc — Loan Management System (Static UI Mockups)

Clickable **static HTML/CSS/JS mockups** that mirror the Angular routes and the
Spring Boot backend. They use **sample data only** (no real API calls) and serve
as the design blueprint for `../angular-app`.

## Run
Open `index.html` in a browser, or serve the folder:
```
npx serve .        # or: python -m http.server 5500
```

## Design system
- Brand palette (Northern Arc): Space Blue `#4fc6e0` (accent), Jet Black `#000104`
  (text), Cool Grey `#939598` / `#d1d3d4` (structure), Star White `#ffffff`.
- Shared theme: `css/style.css`. Shared chrome/helpers: `js/script.js`
  (injects the navbar + footer based on `<body data-area>` and highlights the
  active link via `data-active`).

## Business flow (matches the backend)
`register → apply (loan-application) → staff review → account created → disburse → pay EMIs`

## Pages & backend mapping

### Public
| Page | Endpoint |
|------|----------|
| `index.html` | — (landing) |
| `login.html` | `POST /api/auth/login` |
| `register.html` | `POST /api/auth/register` |
| `contact.html` | — |
| `loan-products.html` | `GET /api/loan-products/active` |

### Customer (USER)
| Page | Endpoint |
|------|----------|
| `customer-dashboard.html` | `/api/customers/me`, `/api/loan-accounts/my`, `/api/loan-applications/my` |
| `apply-loan.html` | `POST /api/loan-applications` |
| `my-applications.html` | `GET /api/loan-applications/my` |
| `loan-accounts.html` | `GET /api/loan-accounts/my` |
| `loan-details.html` | `GET /api/loan-accounts/{id}`, `/api/emi-payments/account/{id}/schedule` |
| `emi-payment.html` | `POST /api/emi-payments` |
| `payment-success.html` | — (confirmation) |
| `profile.html` | `GET/PUT /api/customers/me` |

### Staff (UNDERWRITER / MANAGER / ADMIN)
| Page | Endpoint |
|------|----------|
| `admin-dashboard.html` | `GET /api/dashboard` |
| `customers.html` | `GET /api/customers/summaries` |
| `customer-details.html` | `GET /api/customers/{id}`, `/{id}/summary` |
| `customer-form.html` | `POST/PUT /api/customers` |
| `loan-products-admin.html` | `GET /api/loan-products/paged`, `/search` |
| `loan-product-form.html` | `POST/PUT /api/loan-products` |
| `applications-review.html` | `GET /api/loan-applications/pending`, `PUT /{id}/review` |
| `loan-accounts-admin.html` | `GET /api/loan-accounts`, `POST /{id}/disburse` |
| `emi-payments-admin.html` | `GET /api/emi-payments` |

## Notes
- These are mockups: forms call a small `LMS.mockSubmit()` helper that prevents a
  real POST and redirects to the next screen with a flash message.
- Loan accounts are **not** created directly — they originate from **approving a
  loan application** (see `applications-review.html`).

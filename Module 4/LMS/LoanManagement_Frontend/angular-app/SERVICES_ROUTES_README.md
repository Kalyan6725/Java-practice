# Angular Services and Routes Documentation

## Services Created

All services are located in `src/app/services/` and use the base API URL: `http://localhost:8080/api/loan`

### 1. CustomerService (`customer.service.ts`)
Handles all customer-related operations.

**Methods:**
- `createCustomer(customer)` - POST /customer/create
- `getCustomerById(customerId)` - GET /customer/{customerId}
- `getAllCustomers()` - GET /customers
- `updateCustomer(customer)` - PUT /customer/update
- `deleteCustomer(customerId)` - DELETE /customer/{customerId}
- `getAllCustomerSummaries()` - GET /customers/summaries
- `getCustomerSummary(customerId)` - GET /customer/{customerId}/summary
- `getCustomerSummariesByBranch(branch)` - GET /customers/branch/{branch}/summaries
- `getCustomerSummariesWithMinLoans(minLoans)` - GET /customers/summaries/min-loans

### 2. LoanProductService (`loan-product.service.ts`)
Manages loan products with pagination and search capabilities.

**Methods:**
- `createLoanProduct(loanProduct)` - POST /product/create
- `getLoanProductByCode(loanCode)` - GET /product/{loanCode}
- `getAllLoanProducts()` - GET /products
- `getLoanProductsWithPagination(page, size, sort)` - GET /loan-products
- `searchLoanProducts(searchParams)` - GET /loan-products/search
- `updateLoanProduct(loanProduct)` - PUT /product/update
- `deleteLoanProduct(loanCode)` - DELETE /product/{loanCode}

### 3. LoanAccountService (`loan-account.service.ts`)
Handles loan account operations.

**Methods:**
- `createLoanAccount(loanAccount)` - POST /account/create
- `getLoanAccountById(loanAccountId)` - GET /account/{loanAccountId}
- `getAllLoanAccounts()` - GET /accounts
- `getLoanAccountsByCustomerId(customerId)` - GET /accounts/customer/{customerId}
- `getLoanAccountsByStatus(status)` - GET /accounts/status/{status}
- `updateLoanAccount(loanAccount)` - PUT /account/update
- `deleteLoanAccount(loanAccountId)` - DELETE /account/{loanAccountId}

### 4. EmiPaymentService (`emi-payment.service.ts`)
Manages EMI payment operations.

**Methods:**
- `recordEmiPayment(emiPayment)` - POST /payment/record
- `getEmiPaymentById(paymentId)` - GET /payment/{paymentId}
- `getAllEmiPayments()` - GET /payments
- `getEmiPaymentsByLoanAccountId(loanAccountId)` - GET /payments/account/{loanAccountId}
- `getEmiPaymentsByPaymentType(paymentType)` - GET /payments/type/{paymentType}
- `updateEmiPayment(emiPayment)` - PUT /payment/update
- `deleteEmiPayment(paymentId)` - DELETE /payment/{paymentId}

### 5. DashboardService (`dashboard.service.ts`)
Provides dashboard statistics.

**Methods:**
- `getDashboardData()` - GET /dashboard

## Routes Configuration

Routes are defined in `src/app/app.routes.ts` with lazy loading for better performance.

### Public Routes
- `/` - Landing page
- `/login` - User login
- `/register` - User registration
- `/contact` - Contact page

### Customer Routes
- `/customer/dashboard` - Customer dashboard
- `/customer/loans` - View all loans
- `/customer/loans/:id` - View specific loan details
- `/customer/apply` - Apply for new loan
- `/customer/payments` - View payment history
- `/customer/profile` - View/edit profile

### Admin Routes
- `/admin/dashboard` - Admin dashboard with statistics
- `/admin/customers` - Manage customers list
- `/admin/customers/create` - Create new customer
- `/admin/customers/:id` - View customer details
- `/admin/loan-products` - Manage loan products
- `/admin/loan-products/create` - Create new loan product
- `/admin/loan-accounts` - Manage loan accounts
- `/admin/loan-accounts/create` - Create new loan account
- `/admin/payments` - View all payments

## Usage Example

### Using a Service in a Component

```typescript
import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html'
})
export class CustomersComponent implements OnInit {
  customers: any[] = [];

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getAllCustomers().subscribe({
      next: (response) => {
        if (response.success) {
          this.customers = response.data;
        }
      },
      error: (error) => console.error('Error loading customers:', error)
    });
  }
}
```

## API Response Format

All API responses follow this structure:

```typescript
interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}
```

## Pagination Response Format

Paginated responses include metadata:

```typescript
interface PagedData<T> {
  items: T[];
  page: {
    number: number;
    size: number;
    totalElements: number;
    totalPages: number;
  };
}
```

## Next Steps

1. Create component files for each route
2. Implement forms for create/update operations
3. Add authentication guards for protected routes
4. Implement error handling and loading states
5. Add form validation
6. Create shared UI components (tables, forms, cards)

## Notes

- **No JWT/Authentication**: As requested, no JWT-based authentication or security features have been implemented in the frontend.
- **HttpClient**: Already configured in `app.config.ts`
- **Lazy Loading**: All routes except landing use lazy loading for optimal performance
- **CORS**: Backend has CORS enabled for all origins (`@CrossOrigin(origins = "*")`)

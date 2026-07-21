import { Routes } from '@angular/router';
import { authGuard, staffGuard } from './core/auth.guard';

export const routes: Routes = [
  // ---------- Public ----------
  {
    path: '',
    loadComponent: () =>
      import('./components/public/landing/landing.component').then((m) => m.LandingComponent),
    title: 'Home - Northern Arc',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/public/login/login.component').then((m) => m.LoginComponent),
    title: 'Login - Northern Arc',
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./components/public/register/register.component').then((m) => m.RegisterComponent),
    title: 'Register - Northern Arc',
  },
  {
    path: 'contact',
    loadComponent: () =>
      import('./components/public/contact/contact.component').then((m) => m.ContactComponent),
    title: 'Contact - Northern Arc',
  },
  {
    path: 'loan-products',
    loadComponent: () =>
      import('./components/public/loan-products/loan-products.component').then(
        (m) => m.LoanProductsComponent,
      ),
    title: 'Loan Products - Northern Arc',
  },

  // ---------- Customer ----------
  {
    path: 'customer',
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./components/customer/dashboard/dashboard.component').then(
            (m) => m.CustomerDashboardComponent,
          ),
        title: 'Dashboard - Northern Arc',
      },
      {
        path: 'apply',
        loadComponent: () =>
          import('./components/customer/apply-loan/apply-loan.component').then(
            (m) => m.ApplyLoanComponent,
          ),
        title: 'Apply for a Loan',
      },
      {
        path: 'applications',
        loadComponent: () =>
          import('./components/customer/applications/applications.component').then(
            (m) => m.MyApplicationsComponent,
          ),
        title: 'My Applications',
      },
      {
        path: 'loans',
        loadComponent: () =>
          import('./components/customer/loans/loans.component').then((m) => m.MyLoansComponent),
        title: 'My Loans',
      },
      {
        path: 'loans/:id',
        loadComponent: () =>
          import('./components/customer/loan-details/loan-details.component').then(
            (m) => m.LoanDetailsComponent,
          ),
        title: 'Loan Details',
      },
      {
        path: 'pay/:accountId',
        loadComponent: () =>
          import('./components/customer/emi-payment/emi-payment.component').then(
            (m) => m.EmiPaymentComponent,
          ),
        title: 'Pay EMI',
      },
      {
        path: 'payment-success',
        loadComponent: () =>
          import('./components/customer/payment-success/payment-success.component').then(
            (m) => m.PaymentSuccessComponent,
          ),
        title: 'Payment Successful',
      },
      {
        path: 'profile',
        loadComponent: () =>
          import('./components/customer/profile/profile.component').then((m) => m.ProfileComponent),
        title: 'My Profile',
      },
    ],
  },

  // ---------- Staff (UNDERWRITER / MANAGER / ADMIN) ----------
  {
    path: 'admin',
    canActivate: [authGuard, staffGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./components/admin/dashboard/dashboard.component').then(
            (m) => m.AdminDashboardComponent,
          ),
        title: 'Admin Dashboard',
      },
      {
        path: 'customers',
        loadComponent: () =>
          import('./components/admin/customers/customers.component').then(
            (m) => m.CustomersComponent,
          ),
        title: 'Customers',
      },
      {
        path: 'customers/create',
        loadComponent: () =>
          import('./components/admin/customer-form/customer-form.component').then(
            (m) => m.CustomerFormComponent,
          ),
        title: 'Add Customer',
      },
      {
        path: 'customers/:id',
        loadComponent: () =>
          import('./components/admin/customer-details/customer-details.component').then(
            (m) => m.CustomerDetailsComponent,
          ),
        title: 'Customer Details',
      },
      {
        path: 'loan-products',
        loadComponent: () =>
          import('./components/admin/loan-products/loan-products.component').then(
            (m) => m.AdminLoanProductsComponent,
          ),
        title: 'Manage Loan Products',
      },
      {
        path: 'loan-products/create',
        loadComponent: () =>
          import('./components/admin/loan-product-form/loan-product-form.component').then(
            (m) => m.LoanProductFormComponent,
          ),
        title: 'New Loan Product',
      },
      {
        path: 'loan-products/edit/:code',
        loadComponent: () =>
          import('./components/admin/loan-product-form/loan-product-form.component').then(
            (m) => m.LoanProductFormComponent,
          ),
        title: 'Edit Loan Product',
      },
      {
        path: 'applications',
        loadComponent: () =>
          import('./components/admin/applications-review/applications-review.component').then(
            (m) => m.ApplicationsReviewComponent,
          ),
        title: 'Application Review',
      },
      {
        path: 'loan-accounts',
        loadComponent: () =>
          import('./components/admin/loan-accounts/loan-accounts.component').then(
            (m) => m.AdminLoanAccountsComponent,
          ),
        title: 'Loan Accounts',
      },
      {
        path: 'payments',
        loadComponent: () =>
          import('./components/admin/payments/payments.component').then(
            (m) => m.AdminPaymentsComponent,
          ),
        title: 'EMI Payments',
      },
    ],
  },

  // ---------- Fallback ----------
  { path: '**', redirectTo: '' },
];

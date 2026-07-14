import { Routes } from '@angular/router';
import { LandingComponent } from './components/public/landing/landing.component';

export const routes: Routes = [
  // Public routes
  { path: '', component: LandingComponent, title: 'Home - Loan Management System' },
  { path: 'login', loadComponent: () => import('./components/public/login/login.component').then(m => m.LoginComponent), title: 'Login' },
  { path: 'register', loadComponent: () => import('./components/public/register/register.component').then(m => m.RegisterComponent), title: 'Register' },
  { path: 'contact', loadComponent: () => import('./components/public/contact/contact.component').then(m => m.ContactComponent), title: 'Contact Us' },
  
  // Customer routes
  { path: 'customer/dashboard', loadComponent: () => import('./components/customer/dashboard/dashboard.component').then(m => m.DashboardComponent), title: 'Customer Dashboard' },
  { path: 'customer/loans', loadComponent: () => import('./components/customer/loans/loans.component').then(m => m.LoansComponent), title: 'My Loans' },
  { path: 'customer/loans/:id', loadComponent: () => import('./components/customer/loan-details/loan-details.component').then(m => m.LoanDetailsComponent), title: 'Loan Details' },
  { path: 'customer/apply', loadComponent: () => import('./components/customer/apply-loan/apply-loan.component').then(m => m.ApplyLoanComponent), title: 'Apply for Loan' },
  { path: 'customer/payments', loadComponent: () => import('./components/customer/payments/payments.component').then(m => m.PaymentsComponent), title: 'My Payments' },
  { path: 'customer/profile', loadComponent: () => import('./components/customer/profile/profile.component').then(m => m.ProfileComponent), title: 'My Profile' },
  
  // Admin routes
  { path: 'admin/dashboard', loadComponent: () => import('./components/admin/dashboard/dashboard.component').then(m => m.AdminDashboardComponent), title: 'Admin Dashboard' },
  { path: 'admin/customers', loadComponent: () => import('./components/admin/customers/customers.component').then(m => m.CustomersComponent), title: 'Manage Customers' },
  { path: 'admin/customers/create', loadComponent: () => import('./components/admin/customer-form/customer-form.component').then(m => m.CustomerFormComponent), title: 'Create Customer' },
  { path: 'admin/customers/:id', loadComponent: () => import('./components/admin/customer-details/customer-details.component').then(m => m.CustomerDetailsComponent), title: 'Customer Details' },
  { path: 'admin/loan-products', loadComponent: () => import('./components/admin/loan-products/loan-products.component').then(m => m.LoanProductsComponent), title: 'Manage Loan Products' },
  { path: 'admin/loan-products/create', loadComponent: () => import('./components/admin/loan-product-form/loan-product-form.component').then(m => m.LoanProductFormComponent), title: 'Create Loan Product' },
  { path: 'admin/loan-accounts', loadComponent: () => import('./components/admin/loan-accounts/loan-accounts.component').then(m => m.LoanAccountsComponent), title: 'Manage Loan Accounts' },
  { path: 'admin/loan-accounts/create', loadComponent: () => import('./components/admin/loan-account-form/loan-account-form.component').then(m => m.LoanAccountFormComponent), title: 'Create Loan Account' },
  { path: 'admin/payments', loadComponent: () => import('./components/admin/payments/payments.component').then(m => m.AdminPaymentsComponent), title: 'Manage Payments' },
  
  // Fallback route
  { path: '**', redirectTo: '', pathMatch: 'full' }
];

import { UserRole } from './auth.model';

/** CustomerResponse from the backend. */
export interface Customer {
  customerId: number;
  customerName: string;
  email: string;
  phone: string;
  address: string;
  branch: string;
  role: UserRole;
  status: string;
}

/** CustomerSummaryDTO (aggregated view for staff). */
export interface CustomerSummary {
  customerName: string;
  branch: string;
  numberOfLoans: number;
  totalLoanAmount: number;
  totalPenaltyPaid: number;
}

/** POST /api/customers (ADMIN) - AdminCustomerDTO. */
export interface AdminCustomerRequest {
  customerName: string;
  email: string;
  password: string;
  phone: string;
  address: string;
  branch: string;
  role?: UserRole;
  status?: 'ACTIVE' | 'INACTIVE';
}

/** PUT /api/customers or /api/customers/me - CustomerUpdateRequest. */
export interface CustomerUpdateRequest {
  customerId?: number;
  customerName?: string;
  phone?: string;
  address?: string;
  branch?: string;
  password?: string;
  role?: UserRole;
  status?: 'ACTIVE' | 'INACTIVE';
}

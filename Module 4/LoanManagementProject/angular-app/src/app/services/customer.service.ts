import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}

interface CustomerSummary {
  customerId: number;
  customerName: string;
  email: string;
  branch: string;
  totalLoans: number;
  totalLoanAmount: number;
  activeLoanCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/loan';

  constructor(private http: HttpClient) { }

  createCustomer(customer: any): Observable<ApiResponse<Customer>> {
    return this.http.post<ApiResponse<Customer>>(`${this.apiUrl}/customer/create`, customer);
  }

  getCustomerById(customerId: number): Observable<ApiResponse<Customer>> {
    return this.http.get<ApiResponse<Customer>>(`${this.apiUrl}/customer/${customerId}`);
  }

  getAllCustomers(): Observable<ApiResponse<Customer[]>> {
    return this.http.get<ApiResponse<Customer[]>>(`${this.apiUrl}/customers`);
  }

  updateCustomer(customer: Customer): Observable<ApiResponse<Customer>> {
    return this.http.put<ApiResponse<Customer>>(`${this.apiUrl}/customer/update`, customer);
  }

  deleteCustomer(customerId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/customer/${customerId}`);
  }

  getAllCustomerSummaries(): Observable<ApiResponse<CustomerSummary[]>> {
    return this.http.get<ApiResponse<CustomerSummary[]>>(`${this.apiUrl}/customers/summaries`);
  }

  getCustomerSummary(customerId: number): Observable<ApiResponse<CustomerSummary>> {
    return this.http.get<ApiResponse<CustomerSummary>>(`${this.apiUrl}/customer/${customerId}/summary`);
  }

  getCustomerSummariesByBranch(branch: string): Observable<ApiResponse<CustomerSummary[]>> {
    return this.http.get<ApiResponse<CustomerSummary[]>>(`${this.apiUrl}/customers/branch/${branch}/summaries`);
  }

  getCustomerSummariesWithMinLoans(minLoans: number): Observable<ApiResponse<CustomerSummary[]>> {
    return this.http.get<ApiResponse<CustomerSummary[]>>(`${this.apiUrl}/customers/summaries/min-loans?minLoans=${minLoans}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import {
  Customer,
  CustomerSummary,
  AdminCustomerRequest,
  CustomerUpdateRequest,
} from '../models/customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly baseUrl = `${environment.apiUrl}/customers`;

  constructor(private http: HttpClient) {}

  /** ADMIN: create a customer. */
  create(payload: AdminCustomerRequest): Observable<Customer> {
    return this.http
      .post<ApiResponse<Customer>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  /** MANAGER/ADMIN: list all customers. */
  getAll(): Observable<Customer[]> {
    return this.http
      .get<ApiResponse<Customer[]>>(this.baseUrl)
      .pipe(map((r) => r.data));
  }

  getById(customerId: number): Observable<Customer> {
    return this.http
      .get<ApiResponse<Customer>>(`${this.baseUrl}/${customerId}`)
      .pipe(map((r) => r.data));
  }

  /** Current signed-in customer's profile. */
  getMe(): Observable<Customer> {
    return this.http
      .get<ApiResponse<Customer>>(`${this.baseUrl}/me`)
      .pipe(map((r) => r.data));
  }

  /** ADMIN: update any customer. */
  update(payload: CustomerUpdateRequest): Observable<Customer> {
    return this.http
      .put<ApiResponse<Customer>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  /** Update the current customer's own profile. */
  updateMe(payload: CustomerUpdateRequest): Observable<Customer> {
    return this.http
      .put<ApiResponse<Customer>>(`${this.baseUrl}/me`, payload)
      .pipe(map((r) => r.data));
  }

  delete(customerId: number): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${customerId}`)
      .pipe(map((r) => r.data));
  }

  getAllSummaries(): Observable<CustomerSummary[]> {
    return this.http
      .get<ApiResponse<CustomerSummary[]>>(`${this.baseUrl}/summaries`)
      .pipe(map((r) => r.data));
  }

  getSummary(customerId: number): Observable<CustomerSummary> {
    return this.http
      .get<ApiResponse<CustomerSummary>>(`${this.baseUrl}/${customerId}/summary`)
      .pipe(map((r) => r.data));
  }

  getSummariesByBranch(branch: string): Observable<CustomerSummary[]> {
    return this.http
      .get<ApiResponse<CustomerSummary[]>>(`${this.baseUrl}/branch/${branch}/summaries`)
      .pipe(map((r) => r.data));
  }
}

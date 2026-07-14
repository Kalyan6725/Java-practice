import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CustomerRequestDTO, CustomerResponseDTO } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly API_URL = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Get all customers
   * Endpoint: GET /api/customers
   * Access: ADMIN only
   */
  getAllCustomers(): Observable<CustomerResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.customers.base}`;
    return this.http.get<CustomerResponseDTO[]>(url);
  }

  /**
   * Get customer by ID
   * Endpoint: GET /api/customers/{id}
   * Access: ADMIN only
   */
  getCustomerById(id: number): Observable<CustomerResponseDTO> {
    const url = `${this.API_URL}/api/customers/${id}`;
    return this.http.get<CustomerResponseDTO>(url);
  }

  /**
   * Create new customer
   * Endpoint: POST /api/customers
   * Access: ADMIN only
   */
  createCustomer(customer: CustomerRequestDTO): Observable<CustomerResponseDTO> {
    const url = `${this.API_URL}${environment.endpoints.customers.base}`;
    return this.http.post<CustomerResponseDTO>(url, customer);
  }

  /**
   * Delete customer
   * Endpoint: DELETE /api/customers/{id}
   * Access: ADMIN only
   */
  deleteCustomer(id: number): Observable<{ message: string }> {
    const url = `${this.API_URL}/api/customers/${id}`;
    return this.http.delete<{ message: string }>(url);
  }

  /**
   * Update customer name
   * Endpoint: PUT /api/customers/update/{id}/{name}
   * Access: ADMIN only
   */
  updateCustomerName(id: number, name: string): Observable<number> {
    const url = `${this.API_URL}/api/customers/update/${id}/${name}`;
    return this.http.put<number>(url, {});
  }

  /**
   * Map user to customer
   * Endpoint: PUT /api/admin/user-customer/{username}/{customerId}
   * Access: ADMIN only
   */
  mapUserToCustomer(username: string, customerId: number): Observable<{ message: string }> {
    const url = `${this.API_URL}/api/admin/user-customer/${username}/${customerId}`;
    return this.http.put<{ message: string }>(url, {});
  }
}

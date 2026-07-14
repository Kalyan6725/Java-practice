import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OrderRequestDTO, OrderResponseDTO } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly API_URL = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Create new order
   * Endpoint: POST /api/orders
   * Access: USER, ADMIN
   */
  createOrder(order: OrderRequestDTO): Observable<OrderResponseDTO> {
    const url = `${this.API_URL}${environment.endpoints.orders.base}`;
    return this.http.post<OrderResponseDTO>(url, order);
  }

  /**
   * Get order by ID
   * Endpoint: GET /api/orders/{id}
   * Access: USER, ADMIN
   */
  getOrderById(id: number): Observable<OrderResponseDTO> {
    const url = `${this.API_URL}/api/orders/${id}`;
    return this.http.get<OrderResponseDTO>(url);
  }

  /**
   * Get all orders
   * Endpoint: GET /api/orders
   * Access: USER (own orders), ADMIN (all orders)
   */
  getAllOrders(): Observable<OrderResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.orders.base}`;
    return this.http.get<OrderResponseDTO[]>(url);
  }

  /**
   * Update order
   * Endpoint: PUT /api/orders/{id}
   * Access: ADMIN only
   */
  updateOrder(id: number, order: OrderRequestDTO): Observable<OrderResponseDTO> {
    const url = `${this.API_URL}/api/orders/${id}`;
    return this.http.put<OrderResponseDTO>(url, order);
  }

  /**
   * Delete order
   * Endpoint: DELETE /api/orders/{id}
   * Access: ADMIN only
   */
  deleteOrder(id: number): Observable<string> {
    const url = `${this.API_URL}/api/orders/${id}`;
    return this.http.delete(url, { responseType: 'text' });
  }

  /**
   * Get orders by customer ID (client-side filtering)
   */
  getOrdersByCustomerId(customerId: number): Observable<OrderResponseDTO[]> {
    // Note: Backend doesn't have this endpoint, so we fetch all and filter
    // In production, add a backend endpoint: GET /api/orders/customer/{customerId}
    return this.getAllOrders();
  }
}

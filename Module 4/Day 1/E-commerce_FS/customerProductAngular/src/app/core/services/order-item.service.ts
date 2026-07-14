import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OrderItemRequestDTO, OrderItemResponseDTO } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class OrderItemService {
  private readonly API_URL = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Create new order item
   * Endpoint: POST /api/order-items
   * Access: USER, ADMIN
   */
  createOrderItem(orderItem: OrderItemRequestDTO): Observable<void> {
    const url = `${this.API_URL}${environment.endpoints.orderItems.base}`;
    return this.http.post<void>(url, orderItem);
  }

  /**
   * Get order item by ID
   * Endpoint: GET /api/order-items/{id}
   * Access: USER, ADMIN
   */
  getOrderItemById(id: number): Observable<OrderItemResponseDTO> {
    const url = `${this.API_URL}/api/order-items/${id}`;
    return this.http.get<OrderItemResponseDTO>(url);
  }
}

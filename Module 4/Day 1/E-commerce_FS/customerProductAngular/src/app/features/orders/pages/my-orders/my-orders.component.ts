import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../../core/services/order.service';
import { OrderResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-my-orders',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.css'
})
export class MyOrdersComponent implements OnInit {
  orders: any[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private orderService: OrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.errorMessage = '';
    console.log('🔵 [MyOrders] Loading orders...');
    
    this.orderService.getAllOrders().subscribe({
      next: (orders) => {
        console.log('✅ [MyOrders] Received orders:', orders);
        // Transform backend orders to match template format
        this.orders = orders.map(order => ({
          id: order.id,
          orderDate: new Date(order.orderDate),
          totalAmount: this.calculateOrderTotal(order),
          status: 'Processing',
          itemCount: order.orderItems?.length || 0,
          statusClass: 'warning'
        }));
        this.loading = false;
        this.cdr.detectChanges();
        console.log('✅ [MyOrders] Transformed orders:', this.orders);
      },
      error: (error) => {
        console.error('❌ [MyOrders] Error loading orders:', error);
        console.error('❌ [MyOrders] Error status:', error.status);
        console.error('❌ [MyOrders] Error message:', error.message);
        
        this.loading = false;
        
        // Handle different error cases
        if (error.status === 403) {
          this.errorMessage = 'Please log in to view your orders.';
        } else if (error.status === 404 || error.status === 0) {
          // No orders found or connection issue
          this.orders = [];
          this.errorMessage = '';
        } else {
          this.errorMessage = 'Failed to load orders. Please try again later.';
        }
        
        this.cdr.detectChanges();
      }
    });
  }

  private calculateOrderTotal(order: OrderResponseDTO): number {
    // Backend OrderItemResponseDTO doesn't include price
    // In a production app, you would either:
    // 1. Update backend to include price in OrderItemResponseDTO
    // 2. Fetch product details separately to calculate total
    // For now, return 0 and show item count instead
    return 0;
  }
}

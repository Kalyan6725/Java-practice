import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { OrderService } from '../../../../core/services/order.service';
import { ProductService } from '../../../../core/services/product.service';
import { OrderResponseDTO, ProductResponseDTO } from '../../../../core/models/api.models';

interface OrderItemWithProduct {
  id: number;
  productId: number;
  quantity: number;
  product?: ProductResponseDTO;
}

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css'
})
export class OrderDetailComponent implements OnInit {
  orderId: number = 0;
  order: OrderResponseDTO | null = null;
  orderItems: OrderItemWithProduct[] = [];
  loading = false;
  errorMessage = '';
  
  subtotal = 0;
  tax = 0;
  shipping = 0;
  total = 0;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private productService: ProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.orderId = +params['id'];
      this.loadOrderDetails();
    });
  }

  loadOrderDetails(): void {
    this.loading = true;
    this.errorMessage = '';
    console.log('📝 [OrderDetail] Loading order:', this.orderId);

    this.orderService.getOrderById(this.orderId).subscribe({
      next: (order) => {
        console.log('✅ [OrderDetail] Order loaded:', order);
        this.order = order;
        this.loadProductDetails(order);
      },
      error: (error) => {
        console.error('❌ [OrderDetail] Error loading order:', error);
        this.loading = false;
        if (error.status === 403) {
          this.errorMessage = 'Please log in to view order details.';
        } else if (error.status === 404) {
          this.errorMessage = 'Order not found.';
        } else {
          this.errorMessage = 'Failed to load order details. Please try again.';
        }
        this.cdr.detectChanges();
      }
    });
  }

  loadProductDetails(order: OrderResponseDTO): void {
    if (!order.orderItems || order.orderItems.length === 0) {
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    // Fetch product details for each order item
    const productRequests = order.orderItems.map(item =>
      this.productService.getProductById(item.productId)
    );

    forkJoin(productRequests).subscribe({
      next: (products) => {
        console.log('✅ [OrderDetail] Products loaded:', products);
        
        // Combine order items with product details
        this.orderItems = order.orderItems.map((item, index) => ({
          id: item.id,
          productId: item.productId,
          quantity: item.quantity,
          product: products[index]
        }));

        this.calculateTotals();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('❌ [OrderDetail] Error loading products:', error);
        this.loading = false;
        this.errorMessage = 'Failed to load product details.';
        this.cdr.detectChanges();
      }
    });
  }

  calculateTotals(): void {
    this.subtotal = this.orderItems.reduce((sum, item) => {
      const price = item.product?.price || 0;
      return sum + (price * item.quantity);
    }, 0);
    
    this.tax = this.subtotal * 0.08; // 8% tax
    this.shipping = this.subtotal >= 50 ? 0 : 9.99;
    this.total = this.subtotal + this.tax + this.shipping;
  }

  getOrderStatus(): string {
    // Since backend doesn't have status, return default
    return 'Processing';
  }

  getStatusClass(): string {
    return 'warning'; // Processing status
  }
}

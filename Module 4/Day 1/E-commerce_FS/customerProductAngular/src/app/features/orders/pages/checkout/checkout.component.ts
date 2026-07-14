import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderSummaryComponent } from '../../components/order-summary/order-summary.component';
import { CartService, CartItem } from '../../../../core/services/cart.service';
import { OrderService } from '../../../../core/services/order.service';
import { OrderRequestDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, OrderSummaryComponent],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  cartItems: CartItem[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    public cartService: CartService,
    private orderService: OrderService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
      this.cdr.detectChanges();
    });
  }

  updateQuantity(productId: number, newQuantity: number): void {
    if (newQuantity > 0) {
      this.cartService.updateQuantity(productId, newQuantity);
    }
  }

  increaseQuantity(item: CartItem): void {
    if (item.quantity < item.product.stock) {
      this.cartService.updateQuantity(item.product.id, item.quantity + 1);
    }
  }

  decreaseQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      this.cartService.updateQuantity(item.product.id, item.quantity - 1);
    }
  }

  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId);
  }

  placeOrder(): void {
    if (this.cartItems.length === 0) {
      this.errorMessage = 'Your cart is empty';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    // Create order request DTO
    const orderRequest: OrderRequestDTO = {
      orderDate: new Date().toISOString(),
      customerId: 1, // Replace with actual logged-in customer ID from auth service
      orderItems: this.cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    };

    console.log('📦 [Checkout] Placing order:', orderRequest);

    this.orderService.createOrder(orderRequest).subscribe({
      next: (order) => {
        console.log('✅ [Checkout] Order placed successfully:', order);
        // Clear cart after successful order
        this.cartService.clearCart();
        // Navigate to success page
        this.router.navigate(['/order-success'], { state: { orderId: order.id } });
      },
      error: (error) => {
        console.error('❌ [Checkout] Error placing order:', error);
        if (error.status === 403) {
          this.errorMessage = 'Please log in to place an order.';
        } else {
          this.errorMessage = 'Failed to place order. Please try again.';
        }
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  customer = {
    name: 'John Doe',
    email: 'john.doe@example.com',
    phone: '+1 (555) 123-4567',
    address: '123 Main Street, Apt 4B, New York, NY 10001'
  };
}

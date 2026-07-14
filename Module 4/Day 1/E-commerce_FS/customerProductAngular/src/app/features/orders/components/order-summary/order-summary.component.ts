import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartItem } from '../../../../core/services/cart.service';

@Component({
  selector: 'app-order-summary',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-summary.component.html',
  styleUrl: './order-summary.component.css'
})
export class OrderSummaryComponent implements OnInit, OnChanges {
  @Input() cartItems: CartItem[] = [];
  
  subtotal = 0;
  tax = 0;
  shipping = 0;
  total = 0;

  ngOnInit() {
    this.calculateTotals();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['cartItems']) {
      this.calculateTotals();
    }
  }

  calculateTotals() {
    this.subtotal = this.cartItems.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    this.tax = this.subtotal * 0.08; // 8% tax
    this.shipping = this.subtotal >= 50 ? 0 : 9.99;
    this.total = this.subtotal + this.tax + this.shipping;
  }
}

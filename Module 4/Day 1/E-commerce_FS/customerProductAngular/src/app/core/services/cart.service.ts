import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductResponseDTO } from '../models/api.models';

export interface CartItem {
  product: ProductResponseDTO;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly CART_STORAGE_KEY = 'shopping_cart';
  private cartItemsSubject = new BehaviorSubject<CartItem[]>(this.loadCartFromStorage());
  
  public cartItems$: Observable<CartItem[]> = this.cartItemsSubject.asObservable();

  constructor() { }

  /**
   * Get current cart items
   */
  getCartItems(): CartItem[] {
    return this.cartItemsSubject.value;
  }

  /**
   * Add product to cart
   */
  addToCart(product: ProductResponseDTO, quantity: number = 1): void {
    const currentItems = this.getCartItems();
    const existingItem = currentItems.find(item => item.product.id === product.id);

    if (existingItem) {
      // Update quantity if product already in cart
      existingItem.quantity += quantity;
      // Ensure quantity doesn't exceed stock
      if (existingItem.quantity > product.stock) {
        existingItem.quantity = product.stock;
      }
    } else {
      // Add new item to cart
      currentItems.push({ product, quantity });
    }

    this.updateCart(currentItems);
  }

  /**
   * Remove product from cart
   */
  removeFromCart(productId: number): void {
    const currentItems = this.getCartItems();
    const updatedItems = currentItems.filter(item => item.product.id !== productId);
    this.updateCart(updatedItems);
  }

  /**
   * Update quantity for a product
   */
  updateQuantity(productId: number, quantity: number): void {
    const currentItems = this.getCartItems();
    const item = currentItems.find(item => item.product.id === productId);

    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(productId);
      } else {
        // Ensure quantity doesn't exceed stock
        item.quantity = Math.min(quantity, item.product.stock);
        this.updateCart(currentItems);
      }
    }
  }

  /**
   * Clear entire cart
   */
  clearCart(): void {
    this.updateCart([]);
  }

  /**
   * Get cart item count
   */
  getCartItemCount(): number {
    return this.getCartItems().reduce((count, item) => count + item.quantity, 0);
  }

  /**
   * Get cart subtotal
   */
  getSubtotal(): number {
    return this.getCartItems().reduce(
      (total, item) => total + (item.product.price * item.quantity),
      0
    );
  }

  /**
   * Calculate tax (8%)
   */
  getTax(): number {
    return this.getSubtotal() * 0.08;
  }

  /**
   * Calculate shipping (free if subtotal >= 50)
   */
  getShipping(): number {
    return this.getSubtotal() >= 50 ? 0 : 9.99;
  }

  /**
   * Get cart total
   */
  getTotal(): number {
    return this.getSubtotal() + this.getTax() + this.getShipping();
  }

  /**
   * Check if product is in cart
   */
  isInCart(productId: number): boolean {
    return this.getCartItems().some(item => item.product.id === productId);
  }

  /**
   * Get quantity of product in cart
   */
  getProductQuantity(productId: number): number {
    const item = this.getCartItems().find(item => item.product.id === productId);
    return item ? item.quantity : 0;
  }

  /**
   * Update cart and persist to storage
   */
  private updateCart(items: CartItem[]): void {
    this.cartItemsSubject.next(items);
    this.saveCartToStorage(items);
  }

  /**
   * Load cart from localStorage
   */
  private loadCartFromStorage(): CartItem[] {
    try {
      const stored = localStorage.getItem(this.CART_STORAGE_KEY);
      return stored ? JSON.parse(stored) : [];
    } catch (error) {
      console.error('Error loading cart from storage:', error);
      return [];
    }
  }

  /**
   * Save cart to localStorage
   */
  private saveCartToStorage(items: CartItem[]): void {
    try {
      localStorage.setItem(this.CART_STORAGE_KEY, JSON.stringify(items));
    } catch (error) {
      console.error('Error saving cart to storage:', error);
    }
  }
}

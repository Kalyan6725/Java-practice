import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CartService } from '../../../core/services/cart.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  cartItemCount = 0;
  isAuthenticated = false;
  isAdmin = false;
  username: string | null = null;

  constructor(
    private cartService: CartService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    console.log('🟤 [NavbarComponent] ngOnInit called');
    
    // Subscribe to cart changes
    this.cartService.cartItems$.subscribe(items => {
      console.log('🟤 [NavbarComponent] Cart items updated:', items.length);
      this.cartItemCount = this.cartService.getCartItemCount();
    });

    // Subscribe to auth state changes
    this.authService.currentUser$.subscribe(user => {
      console.log('🟤 [NavbarComponent] Auth state updated, user:', user);
      this.isAuthenticated = this.authService.isAuthenticated();
      this.isAdmin = this.authService.isAdmin();
      this.username = this.authService.getCurrentUsername();
    });
  }

  logout(): void {
    this.authService.logout();
  }
}

import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProductCardComponent } from '../../../../shared/components/product-card/product-card.component';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../../core/services/product.service';
import { ProductResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink, ProductCardComponent],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent implements OnInit {
  featuredProducts: ProductResponseDTO[] = [];
  loading = false;
  errorMessage = '';

  categories = [
    { name: 'Electronics', icon: 'bi-laptop', color: 'primary' },
    { name: 'Mobiles', icon: 'bi-phone', color: 'success' },
    { name: 'Computers', icon: 'bi-pc-display', color: 'info' },
    { name: 'Footwear', icon: 'bi-heart', color: 'danger' },
    { name: 'Home Appliances', icon: 'bi-house', color: 'warning' }
  ];

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadFeaturedProducts();
  }

  loadFeaturedProducts(): void {
    this.loading = true;
    console.log('🏠 [LandingComponent] Starting to load products...');
    console.log('🏠 [LandingComponent] Initial loading state:', this.loading);
    
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        console.log('🏠 [LandingComponent] ✅ SUCCESS - Received products:', products);
        console.log('🏠 [LandingComponent] Number of products:', products?.length);
        
        // Take first 6 products as featured
        this.featuredProducts = products.slice(0, 6);
        this.loading = false;
        
        console.log('🏠 [LandingComponent] Featured products set:', this.featuredProducts.length);
        console.log('🏠 [LandingComponent] Loading state after success:', this.loading);
        
        // Force change detection
        this.cdr.detectChanges();
        console.log('🏠 [LandingComponent] Change detection triggered');
      },
      error: (error) => {
        console.error('🏠 [LandingComponent] ❌ ERROR loading products:', error);
        console.error('🏠 [LandingComponent] Error status:', error.status);
        console.error('🏠 [LandingComponent] Error message:', error.message);
        console.error('🏠 [LandingComponent] Full error:', error);
        
        this.errorMessage = 'Failed to load products';
        this.loading = false;
        this.cdr.detectChanges();
      },
      complete: () => {
        console.log('🏠 [LandingComponent] Observable completed');
      }
    });
  }
}

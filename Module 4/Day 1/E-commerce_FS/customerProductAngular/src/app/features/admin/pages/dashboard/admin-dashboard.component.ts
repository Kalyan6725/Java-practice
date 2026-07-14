import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ProductService } from '../../../../core/services/product.service';
import { OrderService } from '../../../../core/services/order.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  stats = {
    totalProducts: 0,
    totalOrders: 0,
    lowStockProducts: 0,
    recentOrdersCount: 0
  };

  recentOrders: any[] = [];
  lowStockProducts: any[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private productService: ProductService,
    private orderService: OrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.errorMessage = '';
    console.log('🎯 [Dashboard] Loading data...');

    // Load products and orders in parallel using forkJoin
    forkJoin({
      products: this.productService.getAllProducts(),
      orders: this.orderService.getAllOrders()
    }).subscribe({
      next: (result) => {
        console.log('✅ [Dashboard] Data loaded successfully');
        console.log('📦 Products:', result.products.length);
        console.log('📋 Orders:', result.orders.length);
        
        // Process products
        this.stats.totalProducts = result.products.length;
        this.lowStockProducts = result.products.filter(p => p.stock < 10);
        this.stats.lowStockProducts = this.lowStockProducts.length;
        
        // Process orders
        this.stats.totalOrders = result.orders.length;
        this.recentOrders = result.orders
          .sort((a, b) => new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime())
          .slice(0, 5);
        this.stats.recentOrdersCount = this.recentOrders.length;
        
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('❌ [Dashboard] Error loading data:', error);
        this.errorMessage = 'Failed to load dashboard data. Please try again.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}

import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductGridComponent } from '../../components/product-grid/product-grid.component';
import { ProductFiltersComponent } from '../../components/product-filters/product-filters.component';
import { ProductSortComponent } from '../../components/product-sort/product-sort.component';
import { ProductService } from '../../../../core/services/product.service';
import { ProductResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, ProductGridComponent, ProductFiltersComponent, ProductSortComponent],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  products: ProductResponseDTO[] = [];
  allProducts: ProductResponseDTO[] = [];
  totalProducts = 0;
  loading = false;
  errorMessage = '';

  // Filter state
  selectedCategories: string[] = [];
  selectedBrands: string[] = [];
  minPrice = 0;
  maxPrice = 150000;
  inStockOnly = false;

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    console.log('\n' + '='.repeat(80));
    console.log('🔵 [ProductListComponent] ngOnInit called - component initializing');
    console.log('🔵 [ProductListComponent] Backend URL:', this.productService['API_URL']);
    console.log('='.repeat(80) + '\n');
    
    // Check for category query parameter
    this.route.queryParams.subscribe(params => {
      const category = params['category'];
      if (category) {
        console.log('🔵 [ProductListComponent] Category from URL:', category);
        this.selectedCategories = [category];
      }
      this.loadProducts();
    });
  }

  loadProducts(): void {
    console.log('🔵 [ProductListComponent] loadProducts() started');
    this.loading = true;
    this.errorMessage = '';
    console.log('🔵 [ProductListComponent] Initial loading state:', this.loading);
    console.log('🔵 [ProductListComponent] Calling productService.getAllProducts()');

    this.productService.getAllProducts().subscribe({
      next: (products) => {
        console.log('✅ [ProductListComponent] Received products:', products);
        console.log('✅ [ProductListComponent] Number of products:', products?.length);
        this.allProducts = products;
        this.products = products;
        this.totalProducts = products.length;
        this.loading = false;
        console.log('✅ [ProductListComponent] Loading complete - products displayed');
        console.log('✅ [ProductListComponent] Component state:');
        console.log('   - loading:', this.loading);
        console.log('   - errorMessage:', this.errorMessage);
        console.log('   - products.length:', this.products.length);
        console.log('   - Should show grid:', !this.loading && !this.errorMessage && this.products.length > 0);
        
        // Force change detection
        this.cdr.detectChanges();
        console.log('✅ [ProductListComponent] Change detection triggered');
      },
      error: (error) => {
        console.error('❌ [ProductListComponent] Error loading products:', error);
        this.errorMessage = 'Failed to load products. Please try again later.';
        this.loading = false;
        this.cdr.detectChanges();
        console.log('❌ [ProductListComponent] Loading stopped due to error');
      }
    });
  }

  onFilterChange(filters: any): void {
    this.selectedCategories = filters.categories || [];
    this.selectedBrands = filters.brands || [];
    this.minPrice = filters.minPrice || 0;
    this.maxPrice = filters.maxPrice || 5000;
    this.inStockOnly = filters.inStockOnly || false;

    this.applyFilters();
    this.cdr.detectChanges();
  }

  onSortChange(sortOption: string): void {
    this.loading = true;
    this.cdr.detectChanges();
    
    switch(sortOption) {
      case 'price-asc':
        this.productService.sortProductsByPriceAsc().subscribe({
          next: (products) => {
            this.allProducts = products;
            this.applyFilters();
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (error) => {
            console.error('Error sorting products:', error);
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
        break;
      
      case 'price-desc':
        this.productService.sortProductsByPriceDesc().subscribe({
          next: (products) => {
            this.allProducts = products;
            this.applyFilters();
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (error) => {
            console.error('Error sorting products:', error);
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
        break;
      
      case 'name':
        this.productService.sortProductsByName().subscribe({
          next: (products) => {
            this.allProducts = products;
            this.applyFilters();
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (error) => {
            console.error('Error sorting products:', error);
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
        break;
      
      default:
        this.applyFilters();
        this.loading = false;
        this.cdr.detectChanges();
    }
  }

  private applyFilters(): void {
    console.log('🔍 [FILTER DEBUG] Starting applyFilters()');
    console.log('🔍 [FILTER DEBUG] Total products in allProducts:', this.allProducts.length);
    console.log('🔍 [FILTER DEBUG] All products:', this.allProducts);
    console.log('🔍 [FILTER DEBUG] Filter state:', {
      selectedCategories: this.selectedCategories,
      selectedBrands: this.selectedBrands,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      inStockOnly: this.inStockOnly
    });

    let filtered = [...this.allProducts];
    console.log('🔍 [FILTER DEBUG] Initial filtered count:', filtered.length);

    // Filter by category
    if (this.selectedCategories.length > 0) {
      const beforeCount = filtered.length;
      filtered = filtered.filter(p => 
        this.selectedCategories.includes(p.category)
      );
      console.log(`🔍 [FILTER DEBUG] After category filter: ${beforeCount} → ${filtered.length}`);
      console.log('🔍 [FILTER DEBUG] Available categories:', [...new Set(this.allProducts.map(p => p.category))]);
    }

    // Filter by brand
    if (this.selectedBrands.length > 0) {
      const beforeCount = filtered.length;
      filtered = filtered.filter(p => 
        this.selectedBrands.includes(p.brand)
      );
      console.log(`🔍 [FILTER DEBUG] After brand filter: ${beforeCount} → ${filtered.length}`);
      console.log('🔍 [FILTER DEBUG] Available brands:', [...new Set(this.allProducts.map(p => p.brand))]);
      console.log('🔍 [FILTER DEBUG] Selected brands:', this.selectedBrands);
      console.log('🔍 [FILTER DEBUG] Filtered products:', filtered);
    }

    // Filter by price range
    const beforePriceCount = filtered.length;
    filtered = filtered.filter(p => 
      p.price >= this.minPrice && p.price <= this.maxPrice
    );
    console.log(`🔍 [FILTER DEBUG] After price filter (${this.minPrice}-${this.maxPrice}): ${beforePriceCount} → ${filtered.length}`);

    // Filter by stock
    if (this.inStockOnly) {
      const beforeStockCount = filtered.length;
      filtered = filtered.filter(p => p.stock > 0);
      console.log(`🔍 [FILTER DEBUG] After stock filter: ${beforeStockCount} → ${filtered.length}`);
    }

    this.products = filtered;
    this.totalProducts = filtered.length;
    console.log('🔍 [FILTER DEBUG] Final result: showing', this.totalProducts, 'products');
    console.log('🔍 [FILTER DEBUG] Final products:', this.products);
  }
}

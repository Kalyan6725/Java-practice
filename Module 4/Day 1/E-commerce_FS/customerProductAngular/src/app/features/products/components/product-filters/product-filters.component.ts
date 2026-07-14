import { Component, EventEmitter, Output, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../../core/services/product.service';

@Component({
  selector: 'app-product-filters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-filters.component.html',
  styleUrl: './product-filters.component.css'
})
export class ProductFiltersComponent implements OnInit {
  @Output() filtersChanged = new EventEmitter<any>();
  @Input() preSelectedCategories: string[] = [];
  
  categories: string[] = [];
  brands: string[] = [];
  
  selectedCategories: string[] = [];
  selectedBrands: string[] = [];
  minPrice = 0;
  maxPrice = 150000;
  actualMaxPrice = 150000;
  inStockOnly = false;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    console.log('🟣 [ProductFiltersComponent] ngOnInit called');
    console.log('🟣 [ProductFiltersComponent] Pre-selected categories:', this.preSelectedCategories);
    
    // Apply pre-selected categories
    if (this.preSelectedCategories && this.preSelectedCategories.length > 0) {
      this.selectedCategories = [...this.preSelectedCategories];
    }
    
    this.loadFilterOptions();
  }

  loadFilterOptions(): void {
    // Fetch all products to get unique categories and brands
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        // Extract unique categories
        this.categories = [...new Set(products.map(p => p.category))].sort();
        // Extract unique brands
        this.brands = [...new Set(products.map(p => p.brand))].sort();
        
        // Calculate actual max price from products
        if (products.length > 0) {
          const prices = products.map(p => p.price);
          this.actualMaxPrice = Math.ceil(Math.max(...prices) / 1000) * 1000; // Round up to nearest 1000
          this.maxPrice = this.actualMaxPrice;
        }
        
        console.log('🟣 [ProductFiltersComponent] Loaded filter options:');
        console.log('   Categories:', this.categories);
        console.log('   Brands:', this.brands);
        console.log('   Max Price:', this.actualMaxPrice);
        
        // Emit initial filters if categories are pre-selected
        if (this.selectedCategories.length > 0) {
          console.log('🟣 [ProductFiltersComponent] Emitting pre-selected filters');
          this.emitFilters();
        }
      },
      error: (error) => {
        console.error('❌ [ProductFiltersComponent] Error loading filter options:', error);
        // Fallback to empty arrays
        this.categories = [];
        this.brands = [];
      }
    });
  }

  onCategoryChange(category: string, event: any): void {
    if (event.target.checked) {
      this.selectedCategories.push(category);
    } else {
      const index = this.selectedCategories.indexOf(category);
      if (index > -1) {
        this.selectedCategories.splice(index, 1);
      }
    }
    this.emitFilters();
  }

  onBrandChange(brand: string, event: any): void {
    if (event.target.checked) {
      this.selectedBrands.push(brand);
    } else {
      const index = this.selectedBrands.indexOf(brand);
      if (index > -1) {
        this.selectedBrands.splice(index, 1);
      }
    }
    this.emitFilters();
  }

  onPriceChange(): void {
    this.emitFilters();
  }

  onStockChange(): void {
    this.emitFilters();
  }

  private emitFilters(): void {
    this.filtersChanged.emit({
      categories: this.selectedCategories,
      brands: this.selectedBrands,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      inStockOnly: this.inStockOnly
    });
  }

  resetFilters(): void {
    this.selectedCategories = [];
    this.selectedBrands = [];
    this.minPrice = 0;
    this.maxPrice = this.actualMaxPrice;
    this.inStockOnly = false;
    this.emitFilters();
  }
}

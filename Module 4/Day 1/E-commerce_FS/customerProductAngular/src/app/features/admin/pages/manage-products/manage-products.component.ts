import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../../core/services/product.service';
import { ProductResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-manage-products',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './manage-products.component.html',
  styleUrl: './manage-products.component.css'
})
export class ManageProductsComponent implements OnInit {
  products: ProductResponseDTO[] = [];
  filteredProducts: ProductResponseDTO[] = [];
  loading = false;
  errorMessage = '';
  searchTerm = '';
  selectedCategory = '';
  categories: string[] = [];

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = '';

    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.filteredProducts = products;
        this.categories = [...new Set(products.map(p => p.category))];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.errorMessage = 'Failed to load products';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filterProducts(): void {
    this.filteredProducts = this.products.filter(product => {
      const matchesSearch = !this.searchTerm || 
        product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        product.brand.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesCategory = !this.selectedCategory || 
        product.category === this.selectedCategory;

      return matchesSearch && matchesCategory;
    });
  }

  deleteProduct(id: number, name: string): void {
    if (!confirm(`Are you sure you want to delete "${name}"?`)) {
      return;
    }

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.products = this.products.filter(p => p.id !== id);
        this.filterProducts();
        alert('Product deleted successfully!');
      },
      error: (error) => {
        console.error('Error deleting product:', error);
        alert('Failed to delete product. Please try again.');
      }
    });
  }

  getStockClass(stock: number): string {
    if (stock === 0) return 'text-danger';
    if (stock < 10) return 'text-warning';
    return 'text-success';
  }

  getStockText(stock: number): string {
    if (stock === 0) return 'Out of Stock';
    if (stock < 10) return 'Low Stock';
    return 'In Stock';
  }
}

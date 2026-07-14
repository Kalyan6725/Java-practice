import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../../core/services/product.service';
import { CartService } from '../../../../core/services/cart.service';
import { ProductResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {
  product: ProductResponseDTO | null = null;
  quantity = 1;
  loading = false;
  errorMessage = '';
  addedToCart = false;

  relatedProducts: ProductResponseDTO[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Get product ID from route params
    this.route.params.subscribe(params => {
      const productId = +params['id'];
      if (productId) {
        this.loadProduct(productId);
      }
    });
  }

  loadProduct(id: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
        this.loading = false;
        this.cdr.detectChanges();
        this.loadRelatedProducts(product.category);
      },
      error: (error) => {
        console.error('Error loading product:', error);
        this.errorMessage = 'Failed to load product details. Please try again.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadRelatedProducts(category: string): void {
    this.productService.getProductsByCategory(category).subscribe({
      next: (products) => {
        // Filter out current product and take first 3
        this.relatedProducts = products
          .filter(p => p.id !== this.product?.id)
          .slice(0, 3);
      },
      error: (error) => {
        console.error('Error loading related products:', error);
      }
    });
  }

  incrementQuantity(): void {
    if (this.product && this.quantity < this.product.stock) {
      this.quantity++;
    }
  }

  decrementQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(): void {
    if (this.product) {
      this.cartService.addToCart(this.product, this.quantity);
      this.addedToCart = true;
      
      // Reset the message after 3 seconds
      setTimeout(() => {
        this.addedToCart = false;
      }, 3000);
    }
  }

  goToCheckout(): void {
    if (this.product) {
      this.cartService.addToCart(this.product, this.quantity);
      this.router.navigate(['/checkout']);
    }
  }
}

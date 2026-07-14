import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService } from '../../../../core/services/product.service';
import { ProductRequestDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  productForm: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';

  categories = ['Electronics', 'Mobiles', 'Computers', 'Footwear', 'Home Appliances', 'Fashion', 'Books', 'Toys'];
  
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      brand: ['', [Validators.required]],
      category: ['', [Validators.required]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      stock: ['', [Validators.required, Validators.min(0)]]
    });
  }

  get f() {
    return this.productForm.controls;
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const productData: ProductRequestDTO = {
      name: this.productForm.value.name,
      brand: this.productForm.value.brand,
      category: this.productForm.value.category,
      price: parseFloat(this.productForm.value.price),
      stock: parseInt(this.productForm.value.stock)
    };

    this.productService.createProduct(productData).subscribe({
      next: (response) => {
        this.successMessage = 'Product added successfully!';
        this.loading = false;
        
        // Redirect after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/admin/products']);
        }, 2000);
      },
      error: (error) => {
        console.error('Error adding product:', error);
        this.errorMessage = error.error?.message || 'Failed to add product. Please try again.';
        this.loading = false;
      }
    });
  }

  resetForm(): void {
    this.productForm.reset();
    this.errorMessage = '';
    this.successMessage = '';
  }
}

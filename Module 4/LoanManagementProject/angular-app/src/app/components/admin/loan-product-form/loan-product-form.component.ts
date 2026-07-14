import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-product-form',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Create Loan Product</h2>
      <p class="text-muted">Add a new loan product</p>
    </div>
  `,
  styles: []
})
export class LoanProductFormComponent {
}

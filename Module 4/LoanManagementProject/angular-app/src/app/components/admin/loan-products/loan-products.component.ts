import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-products',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Manage Loan Products</h2>
      <p class="text-muted">View and manage loan products</p>
    </div>
  `,
  styles: []
})
export class LoanProductsComponent {
}

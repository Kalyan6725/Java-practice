import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Customer Details</h2>
      <p class="text-muted">View detailed customer information</p>
    </div>
  `,
  styles: []
})
export class CustomerDetailsComponent {
}

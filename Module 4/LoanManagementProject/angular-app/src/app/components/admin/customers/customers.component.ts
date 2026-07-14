import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Manage Customers</h2>
      <p class="text-muted">View and manage all customers</p>
    </div>
  `,
  styles: []
})
export class CustomersComponent {
}

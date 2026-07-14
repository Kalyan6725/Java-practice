import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-payments',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Manage Payments</h2>
      <p class="text-muted">View and manage all EMI payments</p>
    </div>
  `,
  styles: []
})
export class AdminPaymentsComponent {
}

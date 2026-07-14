import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-payments',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>My Payments</h2>
      <p class="text-muted">View your payment history</p>
    </div>
  `,
  styles: []
})
export class PaymentsComponent {
}

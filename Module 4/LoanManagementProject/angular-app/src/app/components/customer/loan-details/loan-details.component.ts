import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-details',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Loan Details</h2>
      <p class="text-muted">Detailed information about your loan</p>
    </div>
  `,
  styles: []
})
export class LoanDetailsComponent {
}

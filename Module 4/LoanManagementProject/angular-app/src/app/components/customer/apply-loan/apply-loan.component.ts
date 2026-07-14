import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apply-loan',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Apply for Loan</h2>
      <p class="text-muted">Fill out the form to apply for a new loan</p>
    </div>
  `,
  styles: []
})
export class ApplyLoanComponent {
}

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-account-form',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Create Loan Account</h2>
      <p class="text-muted">Create a new loan account for a customer</p>
    </div>
  `,
  styles: []
})
export class LoanAccountFormComponent {
}

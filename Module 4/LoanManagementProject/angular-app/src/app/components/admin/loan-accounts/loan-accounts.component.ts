import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-accounts',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Manage Loan Accounts</h2>
      <p class="text-muted">View and manage all loan accounts</p>
    </div>
  `,
  styles: []
})
export class LoanAccountsComponent {
}

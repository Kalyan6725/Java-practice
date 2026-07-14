import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>My Loans</h2>
      <p class="text-muted">View all your loan accounts</p>
    </div>
  `,
  styles: []
})
export class LoansComponent {
}

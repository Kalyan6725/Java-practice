import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Create Customer</h2>
      <p class="text-muted">Add a new customer to the system</p>
    </div>
  `,
  styles: []
})
export class CustomerFormComponent {
}

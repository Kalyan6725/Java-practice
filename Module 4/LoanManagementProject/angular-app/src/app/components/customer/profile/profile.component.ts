import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>My Profile</h2>
      <p class="text-muted">View and update your profile information</p>
    </div>
  `,
  styles: []
})
export class ProfileComponent {
}

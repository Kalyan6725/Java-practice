import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="status-badge" [ngClass]="getBadgeClass()">
      {{ status }}
    </span>
  `,
  styles: []
})
export class StatusBadgeComponent {
  @Input() status: string = '';
  @Input() type: 'loan' | 'payment' | 'application' = 'loan';

  getBadgeClass(): string {
    const statusUpper = this.status.toUpperCase();
    
    if (statusUpper === 'ACTIVE') return 'status-active';
    if (statusUpper === 'CLOSED') return 'status-closed';
    if (statusUpper === 'PENDING') return 'status-pending';
    if (statusUpper === 'REJECTED') return 'status-rejected';
    if (statusUpper === 'SUCCESS') return 'status-active';
    if (statusUpper === 'FAILED') return 'status-rejected';
    if (statusUpper === 'LATE') return 'status-pending';
    
    return 'status-pending';
  }
}

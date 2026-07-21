import { Component, Input } from '@angular/core';

/**
 * Renders a coloured badge for a domain status
 * (ACTIVE, APPROVED, PENDING, REJECTED, PAID, OVERDUE, etc.).
 */
@Component({
  selector: 'app-status-badge',
  standalone: true,
  template: `<span class="badge {{ cssClass }}">{{ status }}</span>`,
})
export class StatusBadgeComponent {
  @Input({ required: true }) status!: string;

  get cssClass(): string {
    const s = (this.status || '').toUpperCase();
    if (['ACTIVE', 'APPROVED', 'PAID', 'CLOSED', 'DISBURSED', 'ACTIVE'].includes(s)) {
      return 'badge-success';
    }
    if (['PENDING', 'SUBMITTED', 'UNDER_REVIEW', 'DUE', 'LATE', 'APPROVED_PENDING'].includes(s)) {
      return 'badge-warning';
    }
    if (['REJECTED', 'DEFAULTED', 'OVERDUE', 'INACTIVE', 'BLOCKED'].includes(s)) {
      return 'badge-danger';
    }
    if (['INFO', 'NEW'].includes(s)) {
      return 'badge-info';
    }
    return 'badge-neutral';
  }
}

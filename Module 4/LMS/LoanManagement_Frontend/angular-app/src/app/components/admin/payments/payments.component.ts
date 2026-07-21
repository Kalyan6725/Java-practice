import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { EmiPaymentService } from '../../../services/emi-payment.service';
import { EmiPayment } from '../../../models/emi-payment.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-admin-payments',
  standalone: true,
  imports: [DatePipe, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head"><div><h1>EMI payments</h1><p class="sub">All recorded EMI collections.</p></div></div>

      @if (loading()) {
        <p class="muted">Loading payments…</p>
      } @else {
        <div class="grid grid-4">
          <div class="stat bar-blue"><span class="label">Total collected</span><span class="value">{{ totalCollected() | inr }}</span></div>
          <div class="stat bar-black"><span class="label">Payments</span><span class="value">{{ payments().length }}</span></div>
          <div class="stat bar-grey"><span class="label">Penalty collected</span><span class="value">{{ totalPenalty() | inr }}</span></div>
          <div class="stat bar-blue"><span class="label">Principal recovered</span><span class="value">{{ totalPrincipal() | inr }}</span></div>
        </div>

        <div class="card mt-3">
          <div class="card-header"><h3>Recent payments</h3></div>
          <div class="table-wrap">
            <table class="table">
              <thead><tr><th>Receipt</th><th>Account</th><th class="num">Inst.</th><th class="num">Amount</th><th class="num">Penalty</th><th>Method</th><th>Date</th><th>Status</th></tr></thead>
              <tbody>
                @for (p of payments(); track p.paymentId) {
                  <tr>
                    <td>PAY-{{ p.paymentId }}</td>
                    <td>{{ p.loanNumber || 'LA-' + p.loanAccountId }}</td>
                    <td class="num">{{ p.installmentNo }}</td>
                    <td class="num">{{ p.totalPaid | inr }}</td>
                    <td class="num">{{ p.penaltyPaid | inr }}</td>
                    <td><span class="badge badge-info">{{ p.paymentType }}</span></td>
                    <td>{{ p.paymentDate | date: 'dd MMM yyyy' }}</td>
                    <td><app-status-badge [status]="p.status" /></td>
                  </tr>
                } @empty {
                  <tr><td colspan="8" class="muted">No payments recorded.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class AdminPaymentsComponent {
  private readonly service = inject(EmiPaymentService);

  readonly payments = signal<EmiPayment[]>([]);
  readonly loading = signal(true);

  readonly totalCollected = computed(() => this.payments().reduce((s, p) => s + (p.totalPaid ?? 0), 0));
  readonly totalPenalty = computed(() => this.payments().reduce((s, p) => s + (p.penaltyPaid ?? 0), 0));
  readonly totalPrincipal = computed(() => this.payments().reduce((s, p) => s + (p.principalPaid ?? 0), 0));

  constructor() {
    this.service.getAll().subscribe({
      next: (data) => {
        this.payments.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { EmiPayment } from '../../../models/emi-payment.model';
import { InrPipe } from '../../../shared/inr.pipe';

@Component({
  selector: 'app-payment-success',
  standalone: true,
  imports: [RouterLink, DatePipe, InrPipe],
  template: `
    <div class="auth-wrap">
      <div class="auth-card card" style="max-width:460px">
        <div class="card-body text-center">
          <div style="width:66px;height:66px;border-radius:50%;background:var(--success-soft);color:var(--success);
                      display:inline-flex;align-items:center;justify-content:center;font-size:2rem;margin-bottom:10px">✓</div>
          <h2 class="mb-0">Payment successful</h2>
          <p class="muted">Your EMI has been recorded.</p>

          @if (payment(); as p) {
            <div class="card" style="text-align:left;box-shadow:none;background:var(--grey-bg)">
              <div class="card-body">
                <dl class="dl" style="grid-template-columns:140px 1fr">
                  <dt>Receipt no.</dt><dd>PAY-{{ p.paymentId }}</dd>
                  <dt>Loan account</dt><dd>{{ p.loanNumber || 'LA-' + p.loanAccountId }}</dd>
                  <dt>Installment</dt><dd>{{ p.installmentNo }}</dd>
                  <dt>Amount</dt><dd>{{ p.totalPaid | inr }}</dd>
                  <dt>Method</dt><dd>{{ p.paymentType }}</dd>
                  <dt>Date</dt><dd>{{ p.paymentDate | date: 'dd MMM yyyy' }}</dd>
                  <dt>Status</dt><dd><span class="badge badge-success">{{ p.status }}</span></dd>
                </dl>
              </div>
            </div>
          }

          <div class="actions mt-2" style="justify-content:center">
            <a routerLink="/customer/loans" class="btn btn-outline">Back to loans</a>
            <a routerLink="/customer/dashboard" class="btn btn-primary">Go to dashboard</a>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class PaymentSuccessComponent {
  private readonly router = inject(Router);
  readonly payment = signal<EmiPayment | null>(
    (this.router.getCurrentNavigation()?.extras.state?.['payment'] as EmiPayment) ??
      (history.state?.['payment'] as EmiPayment) ??
      null,
  );
}

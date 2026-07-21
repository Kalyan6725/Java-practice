import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { LoanAccountService } from '../../../services/loan-account.service';
import { EmiPaymentService } from '../../../services/emi-payment.service';
import { LoanAccount } from '../../../models/loan-account.model';
import { InrPipe } from '../../../shared/inr.pipe';

@Component({
  selector: 'app-emi-payment',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, InrPipe],
  template: `
    <div class="page container">
      <div class="breadcrumb">
        <a [routerLink]="['/customer/loans', accountId]">Loan</a> / Pay EMI
      </div>
      <div class="page-head"><div><h1>Pay EMI</h1><p class="sub">{{ account()?.loanProductName }} • {{ account()?.loanNumber }}</p></div></div>

      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      <div class="row">
        <div class="col">
          <div class="card"><div class="card-body">
            <form [formGroup]="form" (ngSubmit)="submit()">
              <div class="form-group">
                <label>Installment number</label>
                <input class="input" type="number" formControlName="installmentNo" />
                <div class="hint">Leave blank to pay the earliest pending / overdue installment.</div>
              </div>
              <div class="form-group">
                <label>Amount (₹)</label>
                <input class="input" type="number" formControlName="amount" />
                <div class="hint">Defaults to scheduled EMI plus any accrued penalty.</div>
              </div>
              <div class="form-group">
                <label>Payment method <span class="req">*</span></label>
                <select class="select" formControlName="paymentType">
                  <option value="UPI">UPI</option>
                  <option value="CARD">Card</option>
                  <option value="ONLINE">Net banking (Online)</option>
                  <option value="CASH">Cash</option>
                </select>
              </div>
              <div class="actions">
                <button type="submit" class="btn btn-primary btn-lg" [disabled]="submitting()">
                  {{ submitting() ? 'Processing…' : 'Pay now' }}
                </button>
                <a [routerLink]="['/customer/loans', accountId]" class="btn btn-outline btn-lg">Cancel</a>
              </div>
            </form>
          </div></div>
        </div>

        <div class="col" style="max-width:340px">
          <div class="card"><div class="card-body">
            <h3>Payment summary</h3>
            <dl class="dl" style="grid-template-columns:130px 1fr">
              <dt>Scheduled EMI</dt><dd>{{ account()?.emiAmount | inr }}</dd>
              <dt>Outstanding</dt><dd>{{ account()?.outstandingBalance | inr }}</dd>
              <dt>Next due</dt><dd>{{ account()?.nextEmiDate || '—' }}</dd>
            </dl>
          </div></div>
        </div>
      </div>
    </div>
  `,
})
export class EmiPaymentComponent {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly accountService = inject(LoanAccountService);
  private readonly emiService = inject(EmiPaymentService);

  readonly accountId = Number(this.route.snapshot.paramMap.get('accountId'));
  readonly account = signal<LoanAccount | null>(null);
  readonly submitting = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.group({
    installmentNo: [null as number | null, [Validators.min(1)]],
    amount: [null as number | null, [Validators.min(1)]],
    paymentType: ['UPI', [Validators.required]],
  });

  constructor() {
    this.accountService.getById(this.accountId).subscribe({
      next: (acc) => this.account.set(acc),
      error: () => this.error.set('Unable to load loan account.'),
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.error.set(null);
    const raw = this.form.getRawValue();
    this.emiService
      .pay({
        loanAccountId: this.accountId,
        installmentNo: raw.installmentNo ?? undefined,
        amount: raw.amount ?? undefined,
        paymentType: raw.paymentType as 'CASH' | 'CARD' | 'ONLINE' | 'UPI',
      })
      .subscribe({
        next: (payment) => {
          this.submitting.set(false);
          this.router.navigate(['/customer/payment-success'], { state: { payment } });
        },
        error: (err) => {
          this.submitting.set(false);
          this.error.set(err?.error?.message ?? 'Payment failed. Please try again.');
        },
      });
  }
}

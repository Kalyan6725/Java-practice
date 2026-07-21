import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoanAccountService } from '../../../services/loan-account.service';
import { LoanAccount } from '../../../models/loan-account.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-my-loans',
  standalone: true,
  imports: [RouterLink, DatePipe, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>My loans</h1><p class="sub">All your loan accounts in one place.</p></div>
        <a routerLink="/customer/apply" class="btn btn-primary">+ Apply for a loan</a>
      </div>

      @if (loading()) {
        <p class="muted">Loading your loans…</p>
      } @else if (accounts().length === 0) {
        <div class="alert alert-info">You don't have any loan accounts yet. <a routerLink="/customer/apply">Apply for a loan</a>.</div>
      } @else {
        <div class="grid grid-3">
          @for (a of accounts(); track a.loanAccountId) {
            <div class="card"><div class="card-body">
              <div class="flex justify-between items-center">
                <div><b>{{ a.loanNumber || 'LA-' + a.loanAccountId }}</b><div class="muted" style="font-size:.82rem">{{ a.loanProductName }}</div></div>
                <app-status-badge [status]="a.status" />
              </div>
              <hr class="divider" />
              <dl class="dl" style="grid-template-columns:120px 1fr">
                <dt>Principal</dt><dd>{{ a.loanAmount | inr }}</dd>
                <dt>Outstanding</dt><dd>{{ a.outstandingBalance | inr }}</dd>
                <dt>EMI</dt><dd>{{ a.emiAmount ? (a.emiAmount | inr) + ' / mo' : '—' }}</dd>
                <dt>Next due</dt><dd>{{ a.nextEmiDate ? (a.nextEmiDate | date: 'dd MMM yyyy') : '—' }}</dd>
              </dl>
              @if (a.status.toUpperCase() === 'ACTIVE') {
                <a [routerLink]="['/customer/loans', a.loanAccountId]" class="btn btn-outline btn-block mt-2">View details &amp; pay</a>
              } @else {
                <div class="alert alert-warning mt-2 mb-0" style="font-size:.8rem">Pending disbursal. EMIs start after disbursal.</div>
              }
            </div></div>
          }
        </div>
      }
    </div>
  `,
})
export class MyLoansComponent {
  private readonly service = inject(LoanAccountService);
  readonly accounts = signal<LoanAccount[]>([]);
  readonly loading = signal(true);

  constructor() {
    this.service.getMyAccounts().subscribe({
      next: (data) => {
        this.accounts.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

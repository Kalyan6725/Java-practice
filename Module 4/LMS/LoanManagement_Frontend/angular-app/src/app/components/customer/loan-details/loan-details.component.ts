import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { LoanAccountService } from '../../../services/loan-account.service';
import { EmiPaymentService } from '../../../services/emi-payment.service';
import { LoanAccount } from '../../../models/loan-account.model';
import { EmiPayment } from '../../../models/emi-payment.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-loan-details',
  standalone: true,
  imports: [RouterLink, DatePipe, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="breadcrumb"><a routerLink="/customer/loans">My loans</a> / {{ account()?.loanNumber || 'Loan' }}</div>

      @if (loading()) {
        <p class="muted">Loading loan…</p>
      } @else if (!account()) {
        <div class="alert alert-warning">Loan account not found.</div>
      } @else if (account(); as acc) {
        <div class="page-head">
          <div><h1>{{ acc.loanProductName }} • {{ acc.loanNumber || 'LA-' + acc.loanAccountId }}</h1><p class="sub">Account overview and EMI schedule.</p></div>
          @if (acc.status.toUpperCase() === 'ACTIVE') {
            <a [routerLink]="['/customer/pay', acc.loanAccountId]" class="btn btn-primary">Pay next EMI</a>
          }
        </div>

        <div class="grid grid-4">
          <div class="stat bar-blue"><span class="label">Principal</span><span class="value">{{ acc.loanAmount | inr }}</span></div>
          <div class="stat bar-black"><span class="label">Outstanding</span><span class="value">{{ acc.outstandingBalance | inr }}</span></div>
          <div class="stat bar-grey"><span class="label">Monthly EMI</span><span class="value">{{ acc.emiAmount | inr }}</span></div>
          <div class="stat bar-blue"><span class="label">Next due</span><span class="value" style="font-size:1.2rem">{{ acc.nextEmiDate ? (acc.nextEmiDate | date: 'dd MMM yyyy') : '—' }}</span></div>
        </div>

        <div class="row mt-3">
          <div class="col" style="max-width:340px">
            <div class="card"><div class="card-body">
              <h3>Account details</h3>
              <dl class="dl" style="grid-template-columns:130px 1fr">
                <dt>Status</dt><dd><app-status-badge [status]="acc.status" /></dd>
                <dt>Product</dt><dd>{{ acc.loanProductName }} ({{ acc.loanCode }})</dd>
                <dt>Interest</dt><dd>{{ acc.interestRate }}% p.a.</dd>
                <dt>Tenure</dt><dd>{{ acc.tenureMonths }} months</dd>
                <dt>Disbursed on</dt><dd>{{ acc.disbursementDate ? (acc.disbursementDate | date: 'dd MMM yyyy') : '—' }}</dd>
                <dt>EMIs paid</dt><dd>{{ paidCount() }} / {{ acc.tenureMonths }}</dd>
              </dl>
              <div class="progress mt-1"><span [style.width.%]="progress()"></span></div>
            </div></div>
          </div>

          <div class="col">
            <div class="card">
              <div class="card-header"><h3>EMI schedule</h3><span class="tag">{{ schedule().length }} installments</span></div>
              <div class="table-wrap">
                <table class="table">
                  <thead><tr><th>#</th><th>Due date</th><th class="num">EMI</th><th class="num">Penalty</th><th>Status</th><th></th></tr></thead>
                  <tbody>
                    @for (e of schedule(); track e.installmentNo) {
                      <tr>
                        <td>{{ e.installmentNo }}</td>
                        <td>{{ e.dueDate | date: 'dd MMM yyyy' }}</td>
                        <td class="num">{{ e.emiAmount | inr }}</td>
                        <td class="num">{{ e.penaltyPaid | inr }}</td>
                        <td><app-status-badge [status]="e.status" /></td>
                        <td>
                          @if (e.status.toUpperCase() === 'DUE' || e.status.toUpperCase() === 'OVERDUE') {
                            <a [routerLink]="['/customer/pay', acc.loanAccountId]" class="btn btn-primary btn-sm">Pay</a>
                          }
                        </td>
                      </tr>
                    } @empty {
                      <tr><td colspan="6" class="muted">No schedule available.</td></tr>
                    }
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      }
    </div>
  `,
})
export class LoanDetailsComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly accountService = inject(LoanAccountService);
  private readonly emiService = inject(EmiPaymentService);

  readonly account = signal<LoanAccount | null>(null);
  readonly schedule = signal<EmiPayment[]>([]);
  readonly loading = signal(true);

  readonly paidCount = computed(
    () => this.schedule().filter((e) => e.status?.toUpperCase() === 'PAID').length,
  );
  readonly progress = computed(() => {
    const total = this.account()?.tenureMonths ?? 0;
    return total ? Math.round((this.paidCount() / total) * 100) : 0;
  });

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({
      account: this.accountService.getById(id),
      schedule: this.emiService.getSchedule(id),
    }).subscribe({
      next: ({ account, schedule }) => {
        this.account.set(account);
        this.schedule.set(schedule);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

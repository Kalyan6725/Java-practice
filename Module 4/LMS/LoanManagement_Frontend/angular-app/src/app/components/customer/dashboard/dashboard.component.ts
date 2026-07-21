import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../../core/auth.service';
import { LoanAccountService } from '../../../services/loan-account.service';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { LoanAccount } from '../../../models/loan-account.model';
import { LoanApplication } from '../../../models/loan-application.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [RouterLink, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div>
          <h1>Welcome, {{ firstName() }}</h1>
          <p class="sub">Here's an overview of your loans and payments.</p>
        </div>
        <a routerLink="/customer/apply" class="btn btn-primary">+ Apply for a loan</a>
      </div>

      @if (loading()) {
        <p class="muted">Loading your dashboard…</p>
      } @else {
        <div class="grid grid-4">
          <div class="stat bar-blue"><span class="label">Active loans</span><span class="value">{{ activeCount() }}</span></div>
          <div class="stat bar-black"><span class="label">Outstanding</span><span class="value">{{ outstanding() | inr }}</span></div>
          <div class="stat bar-grey"><span class="label">Loan accounts</span><span class="value">{{ accounts().length }}</span></div>
          <div class="stat bar-blue"><span class="label">Applications</span><span class="value">{{ applications().length }}</span></div>
        </div>

        <div class="row mt-3">
          <div class="col">
            <div class="card">
              <div class="card-header"><h3>My loan accounts</h3><a routerLink="/customer/loans" class="btn btn-ghost btn-sm">View all →</a></div>
              <div class="table-wrap">
                <table class="table">
                  <thead><tr><th>Account</th><th>Product</th><th class="num">Principal</th><th>Status</th><th></th></tr></thead>
                  <tbody>
                    @for (a of accounts().slice(0, 5); track a.loanAccountId) {
                      <tr>
                        <td>{{ a.loanNumber || 'LA-' + a.loanAccountId }}</td>
                        <td>{{ a.loanProductName }}</td>
                        <td class="num">{{ a.loanAmount | inr }}</td>
                        <td><app-status-badge [status]="a.status" /></td>
                        <td><a [routerLink]="['/customer/loans', a.loanAccountId]" class="btn btn-outline btn-sm">Details</a></td>
                      </tr>
                    } @empty {
                      <tr><td colspan="5" class="muted">No loan accounts yet.</td></tr>
                    }
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="col" style="max-width:400px">
            <div class="card">
              <div class="card-header"><h3>Recent applications</h3><a routerLink="/customer/applications" class="btn btn-ghost btn-sm">All</a></div>
              <div class="card-body">
                @for (app of applications().slice(0, 4); track app.applicationId) {
                  <div class="flex justify-between items-center">
                    <div>
                      <b>{{ app.loanName }}</b>
                      <div class="muted" style="font-size:.82rem">APP-{{ app.applicationId }} • {{ app.requestedAmount | inr }}</div>
                    </div>
                    <app-status-badge [status]="app.status" />
                  </div>
                  <hr class="divider" />
                } @empty {
                  <p class="muted mb-0">You haven't applied for any loans yet.</p>
                }
                <a routerLink="/customer/apply" class="btn btn-primary btn-block mt-1">Apply for a loan</a>
              </div>
            </div>
          </div>
        </div>
      }
    </div>
  `,
})
export class CustomerDashboardComponent {
  private readonly auth = inject(AuthService);
  private readonly accountService = inject(LoanAccountService);
  private readonly applicationService = inject(LoanApplicationService);

  readonly accounts = signal<LoanAccount[]>([]);
  readonly applications = signal<LoanApplication[]>([]);
  readonly loading = signal(true);

  readonly firstName = computed(() => (this.auth.user()?.customerName ?? 'there').split(' ')[0]);
  readonly activeCount = computed(
    () => this.accounts().filter((a) => a.status?.toUpperCase() === 'ACTIVE').length,
  );
  readonly outstanding = computed(() =>
    this.accounts().reduce((sum, a) => sum + (a.outstandingBalance ?? 0), 0),
  );

  constructor() {
    forkJoin({
      accounts: this.accountService.getMyAccounts(),
      applications: this.applicationService.getMine(),
    }).subscribe({
      next: ({ accounts, applications }) => {
        this.accounts.set(accounts);
        this.applications.set(applications);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

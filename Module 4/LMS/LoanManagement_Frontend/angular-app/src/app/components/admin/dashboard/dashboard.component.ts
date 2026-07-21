import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { DashboardService } from '../../../services/dashboard.service';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { Dashboard } from '../../../models/dashboard.model';
import { LoanApplication } from '../../../models/loan-application.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterLink, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Portfolio dashboard</h1><p class="sub">Lending overview across all branches.</p></div>
        <div class="actions">
          <a routerLink="/admin/applications" class="btn btn-primary">Review applications</a>
          <a routerLink="/admin/loan-products/create" class="btn btn-outline">+ New product</a>
        </div>
      </div>

      @if (loading()) {
        <p class="muted">Loading dashboard…</p>
      } @else {
        <div class="grid grid-4">
          <div class="stat bar-blue"><span class="label">Total customers</span><span class="value">{{ data()?.totalCustomers ?? 0 }}</span></div>
          <div class="stat bar-black"><span class="label">Total loans</span><span class="value">{{ data()?.totalLoans ?? 0 }}</span></div>
          <div class="stat bar-grey"><span class="label">Total disbursed</span><span class="value">{{ data()?.totalLoanAmountDisbursed | inr }}</span></div>
          <div class="stat bar-blue"><span class="label">Penalty collected</span><span class="value">{{ data()?.totalPenaltyCollected | inr }}</span></div>
        </div>
        <div class="grid grid-2 mt-2">
          <div class="stat bar-grey"><span class="label">Top branch</span><span class="value" style="font-size:1.3rem">{{ data()?.topBranch || '—' }}</span></div>
          <div class="stat bar-grey"><span class="label">Highest loan customer</span><span class="value" style="font-size:1.3rem">{{ data()?.highestLoanCustomer || '—' }}</span></div>
        </div>

        <div class="card mt-3">
          <div class="card-header"><h3>Applications awaiting review</h3><a routerLink="/admin/applications" class="btn btn-ghost btn-sm">Open queue →</a></div>
          <div class="table-wrap">
            <table class="table">
              <thead><tr><th>App</th><th>Customer</th><th>Product</th><th class="num">Amount</th><th>Status</th></tr></thead>
              <tbody>
                @for (a of pending().slice(0, 6); track a.applicationId) {
                  <tr>
                    <td>APP-{{ a.applicationId }}</td>
                    <td>{{ a.customerName }}</td>
                    <td>{{ a.loanType }}</td>
                    <td class="num">{{ a.requestedAmount | inr }}</td>
                    <td><app-status-badge [status]="a.status" /></td>
                  </tr>
                } @empty {
                  <tr><td colspan="5" class="muted">No pending applications.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class AdminDashboardComponent {
  private readonly dashboardService = inject(DashboardService);
  private readonly applicationService = inject(LoanApplicationService);

  readonly data = signal<Dashboard | null>(null);
  readonly pending = signal<LoanApplication[]>([]);
  readonly loading = signal(true);

  constructor() {
    forkJoin({
      data: this.dashboardService.getDashboard(),
      pending: this.applicationService.getPending(),
    }).subscribe({
      next: ({ data, pending }) => {
        this.data.set(data);
        this.pending.set(pending);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

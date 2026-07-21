import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { LoanApplication } from '../../../models/loan-application.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-my-applications',
  standalone: true,
  imports: [RouterLink, DatePipe, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>My applications</h1><p class="sub">Track the review status of every loan you've applied for.</p></div>
        <a routerLink="/customer/apply" class="btn btn-primary">+ New application</a>
      </div>

      @if (loading()) {
        <p class="muted">Loading applications…</p>
      } @else {
        <div class="card">
          <div class="table-wrap">
            <table class="table">
              <thead>
                <tr><th>Application</th><th>Product</th><th class="num">Requested</th><th class="num">Tenure</th><th>Applied on</th><th>Status</th><th></th></tr>
              </thead>
              <tbody>
                @for (a of applications(); track a.applicationId) {
                  <tr>
                    <td>APP-{{ a.applicationId }}</td>
                    <td>{{ a.loanName }}</td>
                    <td class="num">{{ a.requestedAmount | inr }}</td>
                    <td class="num">{{ a.tenureMonths }} mo</td>
                    <td>{{ a.applicationDate | date: 'dd MMM yyyy' }}</td>
                    <td><app-status-badge [status]="a.status" /></td>
                    <td>
                      @if (a.loanAccountId) {
                        <a [routerLink]="['/customer/loans', a.loanAccountId]" class="btn btn-outline btn-sm">View loan</a>
                      } @else {
                        <span class="muted" style="font-size:.8rem">—</span>
                      }
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="7" class="muted">You haven't applied for any loans yet.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>

        <div class="alert alert-info mt-3">
          <b>How it works:</b> After you apply, an underwriter reviews your request. Once
          <b>APPROVED</b>, a loan account is created and disbursed — then you can start paying EMIs.
        </div>
      }
    </div>
  `,
})
export class MyApplicationsComponent {
  private readonly service = inject(LoanApplicationService);
  readonly applications = signal<LoanApplication[]>([]);
  readonly loading = signal(true);

  constructor() {
    this.service.getMine().subscribe({
      next: (data) => {
        this.applications.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

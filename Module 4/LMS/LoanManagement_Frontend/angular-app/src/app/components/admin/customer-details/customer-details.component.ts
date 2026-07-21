import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CustomerService } from '../../../services/customer.service';
import { LoanAccountService } from '../../../services/loan-account.service';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { Customer } from '../../../models/customer.model';
import { LoanAccount } from '../../../models/loan-account.model';
import { LoanApplication } from '../../../models/loan-application.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [RouterLink, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="breadcrumb"><a routerLink="/admin/customers">Customers</a> / {{ customer()?.customerName }}</div>

      @if (loading()) {
        <p class="muted">Loading customer…</p>
      } @else if (!customer()) {
        <div class="alert alert-warning">Customer not found.</div>
      } @else if (customer(); as c) {
        <div class="page-head">
          <div><h1>{{ c.customerName }}</h1><p class="sub">Customer #{{ c.customerId }} • {{ c.branch }} branch</p></div>
          <div class="actions">
            <button class="btn btn-danger" (click)="remove(c.customerId)">Delete</button>
          </div>
        </div>

        <div class="row mt-2">
          <div class="col" style="max-width:340px">
            <div class="card"><div class="card-body">
              <h3>Profile</h3>
              <dl class="dl" style="grid-template-columns:110px 1fr">
                <dt>Name</dt><dd>{{ c.customerName }}</dd>
                <dt>Email</dt><dd>{{ c.email }}</dd>
                <dt>Phone</dt><dd>{{ c.phone }}</dd>
                <dt>Address</dt><dd>{{ c.address }}</dd>
                <dt>Branch</dt><dd>{{ c.branch }}</dd>
                <dt>Role</dt><dd><span class="badge badge-info">{{ c.role }}</span></dd>
                <dt>Status</dt><dd><app-status-badge [status]="c.status" /></dd>
              </dl>
            </div></div>
          </div>

          <div class="col">
            <div class="card">
              <div class="card-header"><h3>Loan accounts</h3></div>
              <div class="table-wrap">
                <table class="table">
                  <thead><tr><th>Account</th><th>Product</th><th class="num">Principal</th><th>Status</th></tr></thead>
                  <tbody>
                    @for (a of accounts(); track a.loanAccountId) {
                      <tr><td>{{ a.loanNumber || 'LA-' + a.loanAccountId }}</td><td>{{ a.loanProductName }}</td><td class="num">{{ a.loanAmount | inr }}</td><td><app-status-badge [status]="a.status" /></td></tr>
                    } @empty {
                      <tr><td colspan="4" class="muted">No loan accounts.</td></tr>
                    }
                  </tbody>
                </table>
              </div>
            </div>

            <div class="card mt-2">
              <div class="card-header"><h3>Applications</h3></div>
              <div class="table-wrap">
                <table class="table">
                  <thead><tr><th>App</th><th>Product</th><th class="num">Amount</th><th>Status</th></tr></thead>
                  <tbody>
                    @for (app of applications(); track app.applicationId) {
                      <tr><td>APP-{{ app.applicationId }}</td><td>{{ app.loanName }}</td><td class="num">{{ app.requestedAmount | inr }}</td><td><app-status-badge [status]="app.status" /></td></tr>
                    } @empty {
                      <tr><td colspan="4" class="muted">No applications.</td></tr>
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
export class CustomerDetailsComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly customerService = inject(CustomerService);
  private readonly accountService = inject(LoanAccountService);
  private readonly applicationService = inject(LoanApplicationService);

  private readonly id = Number(this.route.snapshot.paramMap.get('id'));
  readonly customer = signal<Customer | null>(null);
  readonly accounts = signal<LoanAccount[]>([]);
  readonly applications = signal<LoanApplication[]>([]);
  readonly loading = signal(true);

  constructor() {
    forkJoin({
      customer: this.customerService.getById(this.id),
      accounts: this.accountService.getByCustomer(this.id).pipe(catchError(() => of([]))),
      applications: this.applicationService.getByCustomer(this.id).pipe(catchError(() => of([]))),
    }).subscribe({
      next: ({ customer, accounts, applications }) => {
        this.customer.set(customer);
        this.accounts.set(accounts);
        this.applications.set(applications);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  remove(id: number): void {
    if (!confirm('Delete this customer? This cannot be undone.')) return;
    this.customerService.delete(id).subscribe({
      next: () => this.router.navigate(['/admin/customers']),
      error: () => alert('Could not delete customer.'),
    });
  }
}

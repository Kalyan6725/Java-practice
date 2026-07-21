import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoanAccountService } from '../../../services/loan-account.service';
import { LoanAccount } from '../../../models/loan-account.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-admin-loan-accounts',
  standalone: true,
  imports: [FormsModule, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Loan accounts</h1><p class="sub">All approved and disbursed accounts across branches.</p></div>
      </div>

      <div class="card mb-2"><div class="card-body">
        <div class="row" style="gap:12px;align-items:flex-end">
          <div class="col" style="max-width:220px"><label>Status</label>
            <select class="select" [ngModel]="statusFilter()" (ngModelChange)="statusFilter.set($event)">
              <option value="">All</option><option>APPROVED</option><option>ACTIVE</option><option>CLOSED</option><option>DEFAULTED</option>
            </select>
          </div>
          <div class="col"><label>Search</label><input class="input" [ngModel]="search()" (ngModelChange)="search.set($event)" placeholder="Account no. or customer" /></div>
        </div>
      </div></div>

      @if (loading()) {
        <p class="muted">Loading accounts…</p>
      } @else {
        <div class="card">
          <div class="table-wrap">
            <table class="table">
              <thead><tr><th>Account</th><th>Customer</th><th>Product</th><th class="num">Principal</th><th class="num">Outstanding</th><th>Status</th><th></th></tr></thead>
              <tbody>
                @for (a of filtered(); track a.loanAccountId) {
                  <tr>
                    <td>{{ a.loanNumber || 'LA-' + a.loanAccountId }}</td>
                    <td>{{ a.customerName }}</td>
                    <td>{{ a.loanProductName }}</td>
                    <td class="num">{{ a.loanAmount | inr }}</td>
                    <td class="num">{{ a.outstandingBalance | inr }}</td>
                    <td><app-status-badge [status]="a.status" /></td>
                    <td>
                      @if (a.status.toUpperCase() === 'APPROVED') {
                        <button class="btn btn-primary btn-sm" (click)="disburse(a)" [disabled]="disbursing() === a.loanAccountId">
                          {{ disbursing() === a.loanAccountId ? 'Disbursing…' : 'Disburse' }}
                        </button>
                      } @else {
                        <span class="muted" style="font-size:.8rem">—</span>
                      }
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="7" class="muted">No loan accounts found.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class AdminLoanAccountsComponent {
  private readonly service = inject(LoanAccountService);

  readonly accounts = signal<LoanAccount[]>([]);
  readonly loading = signal(true);
  readonly disbursing = signal<number | null>(null);
  readonly statusFilter = signal('');
  readonly search = signal('');

  readonly filtered = computed(() => {
    const s = this.statusFilter().toUpperCase();
    const q = this.search().toLowerCase();
    return this.accounts().filter(
      (a) =>
        (!s || a.status?.toUpperCase() === s) &&
        (!q ||
          (a.loanNumber ?? '').toLowerCase().includes(q) ||
          (a.customerName ?? '').toLowerCase().includes(q)),
    );
  });

  constructor() {
    this.service.getAll().subscribe({
      next: (data) => {
        this.accounts.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  disburse(account: LoanAccount): void {
    if (!confirm(`Disburse account ${account.loanNumber || account.loanAccountId}?`)) return;
    this.disbursing.set(account.loanAccountId);
    this.service.disburse(account.loanAccountId).subscribe({
      next: (updated) => {
        this.accounts.update((list) =>
          list.map((a) => (a.loanAccountId === updated.loanAccountId ? updated : a)),
        );
        this.disbursing.set(null);
      },
      error: () => {
        this.disbursing.set(null);
        alert('Could not disburse the loan.');
      },
    });
  }
}

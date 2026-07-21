import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { Customer } from '../../../models/customer.model';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [FormsModule, RouterLink, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Customers</h1><p class="sub">All registered customers.</p></div>
        <a routerLink="/admin/customers/create" class="btn btn-primary">+ Add customer</a>
      </div>

      <div class="card mb-2"><div class="card-body">
        <div class="row" style="gap:12px;align-items:flex-end">
          <div class="col"><label>Search</label><input class="input" [ngModel]="searchSig()" (ngModelChange)="searchSig.set($event)" placeholder="Name or email" /></div>
          <div class="col"><label>Branch</label>
            <select class="select" [ngModel]="branchSig()" (ngModelChange)="branchSig.set($event)">
              <option value="">All branches</option>
              <option>Bengaluru</option><option>Mumbai</option><option>Delhi</option><option>Chennai</option><option>Hyderabad</option>
            </select>
          </div>
        </div>
      </div></div>

      @if (loading()) {
        <p class="muted">Loading customers…</p>
      } @else {
        <div class="card">
          <div class="table-wrap">
            <table class="table">
              <thead><tr><th>ID</th><th>Customer</th><th>Email</th><th>Branch</th><th>Role</th><th>Status</th><th></th></tr></thead>
              <tbody>
                @for (c of filtered(); track c.customerId) {
                  <tr>
                    <td>#{{ c.customerId }}</td>
                    <td>{{ c.customerName }}</td>
                    <td>{{ c.email }}</td>
                    <td>{{ c.branch }}</td>
                    <td><span class="badge badge-info">{{ c.role }}</span></td>
                    <td><app-status-badge [status]="c.status" /></td>
                    <td><a [routerLink]="['/admin/customers', c.customerId]" class="btn btn-outline btn-sm">View</a></td>
                  </tr>
                } @empty {
                  <tr><td colspan="7" class="muted">No customers found.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class CustomersComponent {
  private readonly service = inject(CustomerService);

  readonly customers = signal<Customer[]>([]);
  readonly loading = signal(true);
  readonly searchSig = signal('');
  readonly branchSig = signal('');

  readonly filtered = computed(() => {
    const q = this.searchSig().toLowerCase();
    const b = this.branchSig();
    return this.customers().filter(
      (c) =>
        (!b || c.branch === b) &&
        (!q || c.customerName.toLowerCase().includes(q) || c.email.toLowerCase().includes(q)),
    );
  });

  constructor() {
    this.service.getAll().subscribe({
      next: (data) => {
        this.customers.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}

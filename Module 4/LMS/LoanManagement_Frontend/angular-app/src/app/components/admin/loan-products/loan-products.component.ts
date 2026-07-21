import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LoanProductService } from '../../../services/loan-product.service';
import { LoanProduct } from '../../../models/loan-product.model';
import { InrPipe } from '../../../shared/inr.pipe';

@Component({
  selector: 'app-admin-loan-products',
  standalone: true,
  imports: [RouterLink, InrPipe],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Loan products</h1><p class="sub">Define and maintain the loan catalogue.</p></div>
        <a routerLink="/admin/loan-products/create" class="btn btn-primary">+ New product</a>
      </div>

      @if (loading()) {
        <p class="muted">Loading products…</p>
      } @else {
        <div class="card">
          <div class="table-wrap">
            <table class="table">
              <thead><tr><th>Code</th><th>Name</th><th>Type</th><th class="num">Amount range</th><th class="num">Interest</th><th class="num">Tenure</th><th>Status</th><th></th></tr></thead>
              <tbody>
                @for (p of products(); track p.loanCode) {
                  <tr>
                    <td>{{ p.loanCode }}</td>
                    <td>{{ p.loanName }}</td>
                    <td><span class="tag">{{ p.loanType }}</span></td>
                    <td class="num">{{ p.minimumAmount | inr }} – {{ p.maximumAmount | inr }}</td>
                    <td class="num">{{ p.interestRate }}%</td>
                    <td class="num">{{ p.minimumTenure }}–{{ p.maximumTenure }} mo</td>
                    <td><span class="badge" [class.badge-success]="p.active" [class.badge-neutral]="!p.active">{{ p.active ? 'Active' : 'Inactive' }}</span></td>
                    <td class="actions">
                      <a [routerLink]="['/admin/loan-products/edit', p.loanCode]" class="btn btn-outline btn-sm">Edit</a>
                      <button class="btn btn-danger btn-sm" (click)="remove(p.loanCode)">Delete</button>
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="8" class="muted">No products yet.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      }
    </div>
  `,
})
export class AdminLoanProductsComponent {
  private readonly service = inject(LoanProductService);
  readonly products = signal<LoanProduct[]>([]);
  readonly loading = signal(true);

  constructor() {
    this.load();
  }

  private load(): void {
    this.loading.set(true);
    this.service.getAll().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  remove(loanCode: string): void {
    if (!confirm(`Delete product ${loanCode}?`)) return;
    this.service.delete(loanCode).subscribe({
      next: () => this.products.update((list) => list.filter((p) => p.loanCode !== loanCode)),
      error: () => alert('Could not delete product.'),
    });
  }
}

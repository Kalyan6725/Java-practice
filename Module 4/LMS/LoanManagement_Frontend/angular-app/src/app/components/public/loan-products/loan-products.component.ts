import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LoanProductService } from '../../../services/loan-product.service';
import { LoanProduct } from '../../../models/loan-product.model';
import { InrPipe } from '../../../shared/inr.pipe';

@Component({
  selector: 'app-public-loan-products',
  standalone: true,
  imports: [RouterLink, InrPipe],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Loan products</h1><p class="sub">Compare our loans and pick what fits your goal.</p></div>
        <a routerLink="/customer/apply" class="btn btn-primary">Apply now</a>
      </div>

      @if (loading()) {
        <p class="muted">Loading products…</p>
      } @else if (error()) {
        <div class="alert alert-warning">{{ error() }}</div>
      } @else if (products().length === 0) {
        <div class="alert alert-info">No active loan products right now. Please check back later.</div>
      } @else {
        <div class="grid grid-3">
          @for (p of products(); track p.loanCode) {
            <div class="card"><div class="card-body">
              <div class="flex justify-between items-center">
                <span class="tag">{{ p.loanType }}</span>
                <span class="badge badge-success">Active</span>
              </div>
              <h3 class="mt-1">{{ p.loanName }}</h3>
              <p class="muted" style="font-size:.82rem">Code: {{ p.loanCode }}</p>
              <dl class="dl" style="grid-template-columns:130px 1fr">
                <dt>Amount</dt><dd>{{ p.minimumAmount | inr }} – {{ p.maximumAmount | inr }}</dd>
                <dt>Interest</dt><dd>{{ p.interestRate }}% p.a.</dd>
                <dt>Tenure</dt><dd>{{ p.minimumTenure }} – {{ p.maximumTenure }} months</dd>
                <dt>Processing</dt><dd>{{ p.processingFee | inr }}</dd>
              </dl>
              <a [routerLink]="['/customer/apply']" [queryParams]="{ code: p.loanCode }"
                 class="btn btn-outline btn-block mt-1">Apply</a>
            </div></div>
          }
        </div>
      }
    </div>
  `,
})
export class LoanProductsComponent {
  private readonly service = inject(LoanProductService);

  readonly products = signal<LoanProduct[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.service.getActive().subscribe({
      next: (data) => {
        this.products.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Unable to load loan products.');
        this.loading.set(false);
      },
    });
  }
}

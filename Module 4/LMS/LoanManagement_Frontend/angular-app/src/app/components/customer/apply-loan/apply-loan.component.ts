import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { LoanProductService } from '../../../services/loan-product.service';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { LoanProduct } from '../../../models/loan-product.model';
import { InrPipe } from '../../../shared/inr.pipe';

@Component({
  selector: 'app-apply-loan',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, InrPipe],
  template: `
    <div class="page container">
      <div class="breadcrumb"><a routerLink="/customer/dashboard">Dashboard</a> / Apply for a loan</div>
      <div class="page-head"><div><h1>Apply for a loan</h1><p class="sub">Pick a product, enter the amount and tenure.</p></div></div>

      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      <div class="row">
        <div class="col">
          <div class="card"><div class="card-body">
            <form [formGroup]="form" (ngSubmit)="submit()">
              <div class="form-group">
                <label>Loan product <span class="req">*</span></label>
                <select class="select" formControlName="loanCode">
                  @for (p of products(); track p.loanCode) {
                    <option [value]="p.loanCode">{{ p.loanName }} ({{ p.loanCode }})</option>
                  }
                </select>
                @if (selected(); as p) {
                  <div class="hint">
                    Amount {{ p.minimumAmount | inr }} – {{ p.maximumAmount | inr }} •
                    Tenure {{ p.minimumTenure }} – {{ p.maximumTenure }} months
                  </div>
                }
              </div>
              <div class="form-row">
                <div class="form-group">
                  <label>Requested amount (₹) <span class="req">*</span></label>
                  <input class="input" type="number" formControlName="requestedAmount" />
                  @if (invalid('requestedAmount')) {
                    <div class="hint" style="color:var(--danger)">Enter a positive amount within the product limits.</div>
                  }
                </div>
                <div class="form-group">
                  <label>Tenure (months) <span class="req">*</span></label>
                  <input class="input" type="number" formControlName="tenureMonths" />
                  @if (invalid('tenureMonths')) {
                    <div class="hint" style="color:var(--danger)">Enter a tenure between 1 and 600 months.</div>
                  }
                </div>
              </div>
              <div class="actions">
                <button type="submit" class="btn btn-primary btn-lg" [disabled]="submitting()">
                  {{ submitting() ? 'Submitting…' : 'Submit application' }}
                </button>
                <a routerLink="/customer/dashboard" class="btn btn-outline btn-lg">Cancel</a>
              </div>
            </form>
          </div></div>
        </div>

        <div class="col" style="max-width:340px">
          <div class="card"><div class="card-body">
            <h3>Estimated EMI</h3>
            <p class="muted" style="font-size:.84rem">Indicative only. Final terms confirmed after review.</p>
            <div class="stat bar-blue" style="box-shadow:none;border:none;padding:0">
              <span class="label">Monthly EMI</span>
              <span class="value accent">{{ emi() | inr }}</span>
            </div>
            <hr class="divider" />
            <dl class="dl" style="grid-template-columns:120px 1fr">
              <dt>Interest rate</dt><dd>{{ selected()?.interestRate ?? 0 }}% p.a.</dd>
              <dt>Total interest</dt><dd>{{ totalInterest() | inr }}</dd>
              <dt>Total payable</dt><dd>{{ totalPayable() | inr }}</dd>
            </dl>
          </div></div>
        </div>
      </div>
    </div>
  `,
})
export class ApplyLoanComponent {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(LoanProductService);
  private readonly applicationService = inject(LoanApplicationService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly products = signal<LoanProduct[]>([]);
  readonly submitting = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    loanCode: ['', [Validators.required]],
    requestedAmount: [100000, [Validators.required, Validators.min(1), Validators.max(10000000)]],
    tenureMonths: [24, [Validators.required, Validators.min(1), Validators.max(600)]],
  });

  private readonly formValue = toSignal(this.form.valueChanges, { initialValue: this.form.getRawValue() });

  readonly selected = computed(() =>
    this.products().find((p) => p.loanCode === this.formValue().loanCode),
  );

  readonly emi = computed(() => {
    const p = this.selected();
    const amount = Number(this.formValue().requestedAmount) || 0;
    const n = Number(this.formValue().tenureMonths) || 1;
    if (!p) return 0;
    const r = p.interestRate / 12 / 100;
    if (r === 0) return Math.round(amount / n);
    const emi = (amount * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
    return Math.round(emi);
  });

  readonly totalPayable = computed(() => this.emi() * (Number(this.formValue().tenureMonths) || 0));
  readonly totalInterest = computed(
    () => this.totalPayable() - (Number(this.formValue().requestedAmount) || 0),
  );

  invalid(control: string): boolean {
    const c = this.form.get(control);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  constructor() {
    this.productService.getActive().subscribe({
      next: (data) => {
        this.products.set(data);
        const preset = this.route.snapshot.queryParamMap.get('code');
        const code = preset && data.some((p) => p.loanCode === preset) ? preset : data[0]?.loanCode;
        if (code) this.form.patchValue({ loanCode: code });
      },
      error: () => this.error.set('Unable to load loan products.'),
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.error.set(null);
    this.applicationService.apply(this.form.getRawValue()).subscribe({
      next: () => {
        this.submitting.set(false);
        this.router.navigate(['/customer/applications']);
      },
      error: (err) => {
        this.submitting.set(false);
        this.error.set(err?.error?.message ?? 'Could not submit your application.');
      },
    });
  }
}

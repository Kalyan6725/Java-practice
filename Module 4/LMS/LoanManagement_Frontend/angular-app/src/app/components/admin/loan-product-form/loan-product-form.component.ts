import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { LoanProductService } from '../../../services/loan-product.service';
import { LoanProductRequest, LoanType } from '../../../models/loan-product.model';

@Component({
  selector: 'app-loan-product-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="page container">
      <div class="breadcrumb"><a routerLink="/admin/loan-products">Products</a> / {{ editMode() ? 'Edit' : 'New' }} product</div>
      <div class="page-head"><div><h1>{{ editMode() ? 'Edit' : 'New' }} loan product</h1><p class="sub">Configure limits, pricing and penalties.</p></div></div>

      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      <div class="card" style="max-width:820px"><div class="card-body">
        <form [formGroup]="form" (ngSubmit)="submit()">
          <div class="form-row">
            <div class="form-group"><label>Loan code <span class="req">*</span></label>
              <input class="input" formControlName="loanCode" [readonly]="editMode()" />
              @if (invalid('loanCode')) { <div class="hint" style="color:var(--danger)">3–20 characters.</div> }
            </div>
            <div class="form-group"><label>Loan name <span class="req">*</span></label>
              <input class="input" formControlName="loanName" />
              @if (invalid('loanName')) { <div class="hint" style="color:var(--danger)">2–100 characters.</div> }
            </div>
          </div>
          <div class="form-group"><label>Loan type <span class="req">*</span></label>
            <select class="select" formControlName="loanType">
              <option value="PERSONAL">PERSONAL</option><option value="HOME">HOME</option>
              <option value="VEHICLE">VEHICLE</option><option value="EDUCATION">EDUCATION</option>
              <option value="BUSINESS">BUSINESS</option>
            </select>
          </div>

          <hr class="divider" />
          <h3>Amount &amp; tenure</h3>
          <div class="form-row">
            <div class="form-group"><label>Minimum amount (₹) <span class="req">*</span></label><input class="input" type="number" formControlName="minimumAmount" /></div>
            <div class="form-group"><label>Maximum amount (₹) <span class="req">*</span></label><input class="input" type="number" formControlName="maximumAmount" /></div>
          </div>
          <div class="form-row">
            <div class="form-group"><label>Minimum tenure (months) <span class="req">*</span></label><input class="input" type="number" formControlName="minimumTenure" /></div>
            <div class="form-group"><label>Maximum tenure (months) <span class="req">*</span></label><input class="input" type="number" formControlName="maximumTenure" /></div>
          </div>

          <hr class="divider" />
          <h3>Pricing</h3>
          <div class="form-row">
            <div class="form-group"><label>Interest rate (% p.a.) <span class="req">*</span></label><input class="input" type="number" step="0.1" formControlName="interestRate" /></div>
            <div class="form-group"><label>Processing fee (₹) <span class="req">*</span></label><input class="input" type="number" formControlName="processingFee" /></div>
          </div>
          <div class="form-row">
            <div class="form-group"><label>Daily penalty rate (%)</label><input class="input" type="number" step="0.01" formControlName="dailyPenaltyRate" /></div>
            <div class="form-group"><label>Status</label>
              <select class="select" formControlName="active"><option [ngValue]="true">Active</option><option [ngValue]="false">Inactive</option></select>
            </div>
          </div>

          <div class="actions mt-2">
            <button type="submit" class="btn btn-primary btn-lg" [disabled]="saving()">{{ saving() ? 'Saving…' : 'Save product' }}</button>
            <a routerLink="/admin/loan-products" class="btn btn-outline btn-lg">Cancel</a>
          </div>
        </form>
      </div></div>
    </div>
  `,
})
export class LoanProductFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(LoanProductService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly editMode = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    loanCode: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    loanName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    loanType: ['PERSONAL' as LoanType, [Validators.required]],
    minimumAmount: [50000, [Validators.required, Validators.min(1), Validators.max(10000000)]],
    maximumAmount: [1000000, [Validators.required, Validators.min(1), Validators.max(10000000)]],
    minimumTenure: [6, [Validators.required, Validators.min(1), Validators.max(600)]],
    maximumTenure: [60, [Validators.required, Validators.min(1), Validators.max(600)]],
    interestRate: [11.5, [Validators.required, Validators.min(0), Validators.max(100)]],
    processingFee: [2500, [Validators.required, Validators.min(0), Validators.max(1000000)]],
    dailyPenaltyRate: [0.05, [Validators.min(0), Validators.max(100)]],
    active: [true],
  });

  invalid(control: string): boolean {
    const c = this.form.get(control);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  constructor() {
    const code = this.route.snapshot.paramMap.get('code');
    if (code) {
      this.editMode.set(true);
      this.service.getByCode(code).subscribe({
        next: (p) => this.form.patchValue(p),
        error: () => this.error.set('Unable to load product.'),
      });
    }
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const payload = this.form.getRawValue() as LoanProductRequest;
    const call = this.editMode() ? this.service.update(payload) : this.service.create(payload);
    call.subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/admin/loan-products']);
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(err?.error?.message ?? 'Could not save product.');
      },
    });
  }
}

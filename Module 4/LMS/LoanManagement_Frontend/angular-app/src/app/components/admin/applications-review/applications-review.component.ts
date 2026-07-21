import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoanApplicationService } from '../../../services/loan-application.service';
import { LoanApplication, ReviewDecision } from '../../../models/loan-application.model';
import { InrPipe } from '../../../shared/inr.pipe';
import { StatusBadgeComponent } from '../../../shared/status-badge.component';

@Component({
  selector: 'app-applications-review',
  standalone: true,
  imports: [ReactiveFormsModule, DatePipe, InrPipe, StatusBadgeComponent],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Application review queue</h1><p class="sub">Approve or reject pending loan applications.</p></div>
        <span class="badge badge-warning" style="font-size:.85rem;padding:6px 12px">{{ queue().length }} pending</span>
      </div>

      @if (message()) { <div class="alert alert-success">{{ message() }}</div> }
      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      @if (loading()) {
        <p class="muted">Loading queue…</p>
      } @else {
        <div class="row">
          <div class="col" style="max-width:340px">
            <div class="card">
              <div class="card-header"><h3>Pending</h3></div>
              <div class="card-body" style="padding:0">
                @for (a of queue(); track a.applicationId) {
                  <a href="javascript:void(0)" (click)="select(a)"
                     class="flex justify-between items-center"
                     style="padding:14px 18px;border-bottom:1px solid var(--grey-6);color:inherit"
                     [style.background]="selected()?.applicationId === a.applicationId ? 'var(--space-blue-soft)' : ''">
                    <div><b>APP-{{ a.applicationId }}</b><div class="muted" style="font-size:.8rem">{{ a.customerName }} • {{ a.loanType }}</div></div>
                    <b>{{ a.requestedAmount | inr }}</b>
                  </a>
                } @empty {
                  <div class="card-body muted">No pending applications.</div>
                }
              </div>
            </div>
          </div>

          <div class="col">
            @if (selected(); as s) {
              <div class="card"><div class="card-body">
                <div class="flex justify-between items-center">
                  <div><h3 class="mb-0">APP-{{ s.applicationId }} • {{ s.loanName }}</h3><p class="muted mb-0">Submitted {{ s.applicationDate | date: 'dd MMM yyyy' }}</p></div>
                  <app-status-badge [status]="s.status" />
                </div>
                <hr class="divider" />
                <dl class="dl" style="grid-template-columns:150px 1fr">
                  <dt>Applicant</dt><dd>{{ s.customerName }} (#{{ s.customerId }})</dd>
                  <dt>Product</dt><dd>{{ s.loanName }} ({{ s.loanCode }})</dd>
                  <dt>Requested amount</dt><dd>{{ s.requestedAmount | inr }}</dd>
                  <dt>Tenure</dt><dd>{{ s.tenureMonths }} months</dd>
                </dl>

                <hr class="divider" />
                <form [formGroup]="form" (ngSubmit)="submit(s.applicationId)">
                  <div class="form-group">
                    <label>Decision <span class="req">*</span></label>
                    <select class="select" formControlName="decision">
                      <option value="APPROVED">Approve</option>
                      <option value="REJECTED">Reject</option>
                      <option value="UNDER_REVIEW">Mark under review</option>
                    </select>
                  </div>
                  @if (form.value.decision === 'APPROVED') {
                    <div class="form-group">
                      <label>Approved amount (₹)</label>
                      <input class="input" type="number" formControlName="approvedAmount" />
                      <div class="hint">Defaults to the requested amount if left blank.</div>
                    </div>
                  }
                  <div class="form-group">
                    <label>Remarks</label>
                    <textarea class="textarea" formControlName="remarks" placeholder="Notes for the applicant / audit trail"></textarea>
                  </div>
                  <div class="actions">
                    <button type="submit" class="btn btn-primary btn-lg" [disabled]="submitting()">{{ submitting() ? 'Submitting…' : 'Submit decision' }}</button>
                  </div>
                  <p class="hint mt-1">On approval, a loan account is created automatically and can then be disbursed from the Accounts page.</p>
                </form>
              </div></div>
            } @else {
              <div class="card"><div class="card-body muted">Select an application from the queue to review it.</div></div>
            }
          </div>
        </div>
      }
    </div>
  `,
})
export class ApplicationsReviewComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(LoanApplicationService);

  readonly queue = signal<LoanApplication[]>([]);
  readonly selected = signal<LoanApplication | null>(null);
  readonly loading = signal(true);
  readonly submitting = signal(false);
  readonly message = signal<string | null>(null);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    decision: ['APPROVED' as ReviewDecision, [Validators.required]],
    approvedAmount: [null as number | null],
    remarks: ['', [Validators.maxLength(500)]],
  });

  constructor() {
    this.load();
  }

  private load(): void {
    this.loading.set(true);
    this.service.getPending().subscribe({
      next: (data) => {
        this.queue.set(data);
        this.selected.set(data[0] ?? null);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  select(a: LoanApplication): void {
    this.selected.set(a);
    this.message.set(null);
  }

  submit(applicationId: number): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting.set(true);
    this.error.set(null);
    const raw = this.form.getRawValue();
    this.service
      .review(applicationId, {
        decision: raw.decision,
        remarks: raw.remarks || undefined,
        approvedAmount: raw.approvedAmount ?? undefined,
      })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.message.set(`Application APP-${applicationId} reviewed successfully.`);
          this.form.reset({ decision: 'APPROVED', approvedAmount: null, remarks: '' });
          this.load();
        },
        error: (err) => {
          this.submitting.set(false);
          this.error.set(err?.error?.message ?? 'Could not submit the decision.');
        },
      });
  }
}

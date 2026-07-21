import { Component, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { inject } from '@angular/core';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="page container">
      <div class="page-head">
        <div><h1>Contact us</h1><p class="sub">We're here to help with your loans and payments.</p></div>
      </div>

      <div class="row">
        <div class="col">
          <div class="card"><div class="card-body">
            <h3>Send us a message</h3>
            @if (sent()) {
              <div class="alert alert-success">Thanks! We'll get back to you shortly.</div>
            }
            <form [formGroup]="form" (ngSubmit)="submit()">
              <div class="form-row">
                <div class="form-group"><label>Name <span class="req">*</span></label><input class="input" formControlName="name" /></div>
                <div class="form-group"><label>Email <span class="req">*</span></label><input class="input" type="email" formControlName="email" /></div>
              </div>
              <div class="form-group"><label>Subject</label><input class="input" formControlName="subject" placeholder="How can we help?" /></div>
              <div class="form-group"><label>Message <span class="req">*</span></label><textarea class="textarea" formControlName="message"></textarea></div>
              <button type="submit" class="btn btn-primary" [disabled]="form.invalid">Send message</button>
            </form>
          </div></div>
        </div>

        <div class="col" style="max-width:360px">
          <div class="card"><div class="card-body">
            <h3>Head office</h3>
            <dl class="dl">
              <dt>Address</dt><dd>Northern Arc Tower, MG Road, Bengaluru 560001</dd>
              <dt>Phone</dt><dd>+91 80 4000 0000</dd>
              <dt>Email</dt><dd>support&#64;northernarc.example</dd>
              <dt>Hours</dt><dd>Mon–Sat, 9:00–18:00</dd>
            </dl>
          </div></div>
          <div class="card mt-2"><div class="card-body">
            <h3>Branches</h3>
            <p class="muted mb-0">Bengaluru • Mumbai • Delhi • Chennai • Hyderabad</p>
          </div></div>
        </div>
      </div>
    </div>
  `,
})
export class ContactComponent {
  private readonly fb = inject(FormBuilder);
  readonly sent = signal(false);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    subject: [''],
    message: ['', [Validators.required]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    // Static contact form: no backend endpoint.
    this.sent.set(true);
    this.form.reset();
  }
}

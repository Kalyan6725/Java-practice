import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="hero">
      <div class="container">
        <h1>Smart lending, <span class="accent">simplified.</span></h1>
        <p>
          Apply for personal, home, vehicle, education and business loans in minutes. Track
          applications, manage EMIs and stay on top of your finances with Northern Arc.
        </p>
        <div class="cta">
          <a routerLink="/register" class="btn btn-primary btn-lg">Get started</a>
          <a routerLink="/loan-products" class="btn btn-outline btn-lg"
             style="color:#fff;border-color:#3a3d44">Explore loans</a>
        </div>
      </div>
    </section>

    <section class="container" style="margin-top:-32px;position:relative;z-index:2">
      <div class="grid grid-4">
        <div class="stat bar-blue"><span class="label">Products</span><span class="value">5</span><span class="foot">Loan types available</span></div>
        <div class="stat bar-black"><span class="label">Disbursed</span><span class="value">₹42.5 Cr</span><span class="foot">Across all branches</span></div>
        <div class="stat bar-grey"><span class="label">Customers</span><span class="value">3,200+</span><span class="foot">And growing</span></div>
        <div class="stat bar-blue"><span class="label">Approval time</span><span class="value">24h</span><span class="foot">Average turnaround</span></div>
      </div>
    </section>

    <section class="page container">
      <div class="page-head">
        <div><h2>Why Northern Arc</h2><p class="sub">Everything you need to borrow and repay with confidence.</p></div>
      </div>
      <div class="grid grid-3">
        <div class="card"><div class="card-body feature"><div class="icon">₹</div><h3>Transparent pricing</h3><p class="muted">Clear interest rates, processing fees and penalty terms on every product.</p></div></div>
        <div class="card"><div class="card-body feature"><div class="icon">⏲</div><h3>Fast approvals</h3><p class="muted">Submit an application and track its review status in real time.</p></div></div>
        <div class="card"><div class="card-body feature"><div class="icon">↻</div><h3>Easy EMIs</h3><p class="muted">Pay by UPI, card, net-banking or cash and view your full schedule.</p></div></div>
      </div>

      <div class="page-head mt-3">
        <div><h2>Our loan products</h2><p class="sub">A quick look at what you can apply for today.</p></div>
        <a routerLink="/loan-products" class="btn btn-ghost">View all →</a>
      </div>
      <div class="grid grid-3">
        <div class="card"><div class="card-body"><span class="tag">PERSONAL</span><h3 class="mt-1">Personal Loan</h3><p class="muted mb-0">Up to ₹10,00,000 • from 11.5% p.a. • 6–60 months</p></div></div>
        <div class="card"><div class="card-body"><span class="tag">HOME</span><h3 class="mt-1">Home Loan</h3><p class="muted mb-0">Up to ₹1,00,00,000 • from 8.4% p.a. • 60–360 months</p></div></div>
        <div class="card"><div class="card-body"><span class="tag">VEHICLE</span><h3 class="mt-1">Vehicle Loan</h3><p class="muted mb-0">Up to ₹25,00,000 • from 9.6% p.a. • 12–84 months</p></div></div>
      </div>
    </section>
  `,
})
export class LandingComponent {}

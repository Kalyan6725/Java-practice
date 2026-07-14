import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Admin Dashboard</h2>
      <div class="row mt-4">
        <div class="col-md-3">
          <div class="card">
            <div class="card-body">
              <h5>Total Customers</h5>
              <h3>0</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card">
            <div class="card-body">
              <h5>Total Loans</h5>
              <h3>0</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card">
            <div class="card-body">
              <h5>Loan Products</h5>
              <h3>0</h3>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card">
            <div class="card-body">
              <h5>Total Disbursed</h5>
              <h3>₹0</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class AdminDashboardComponent {
}

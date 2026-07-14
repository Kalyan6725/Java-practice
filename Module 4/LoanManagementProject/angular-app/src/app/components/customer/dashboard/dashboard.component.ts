import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container py-4">
      <h2>Customer Dashboard</h2>
      <div class="row mt-4">
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5>Active Loans</h5>
              <h3>0</h3>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5>Total Loan Amount</h5>
              <h3>₹0</h3>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card">
            <div class="card-body">
              <h5>Pending EMIs</h5>
              <h3>0</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class DashboardComponent {
}

import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../../../core/services/customer.service';
import { CustomerResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-manage-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-customers.component.html',
  styleUrl: './manage-customers.component.css'
})
export class ManageCustomersComponent implements OnInit {
  customers: CustomerResponseDTO[] = [];
  filteredCustomers: CustomerResponseDTO[] = [];
  loading = false;
  errorMessage = '';
  searchTerm = '';

  constructor(
    private customerService: CustomerService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading = true;
    this.errorMessage = '';

    this.customerService.getAllCustomers().subscribe({
      next: (customers) => {
        this.customers = customers;
        this.filteredCustomers = customers;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading customers:', error);
        if (error.status === 403) {
          this.errorMessage = 'You do not have permission to view customers.';
        } else {
          this.errorMessage = 'Failed to load customers. Please try again.';
        }
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filterCustomers(): void {
    this.filteredCustomers = this.customers.filter(customer => {
      const matchesSearch = !this.searchTerm || 
        customer.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        customer.id.toString().includes(this.searchTerm);

      return matchesSearch;
    });
  }

  deleteCustomer(id: number, name: string): void {
    if (!confirm(`Are you sure you want to delete customer "${name}"? This action cannot be undone.`)) {
      return;
    }

    this.customerService.deleteCustomer(id).subscribe({
      next: () => {
        this.customers = this.customers.filter(c => c.id !== id);
        this.filterCustomers();
        alert('Customer deleted successfully!');
      },
      error: (error) => {
        console.error('Error deleting customer:', error);
        alert('Failed to delete customer. Please try again.');
      }
    });
  }

  getOrderCount(customer: CustomerResponseDTO): number {
    return customer.orders?.length || 0;
  }
}

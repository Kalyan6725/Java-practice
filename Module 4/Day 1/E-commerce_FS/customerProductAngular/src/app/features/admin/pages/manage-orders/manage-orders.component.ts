import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../../core/services/order.service';
import { OrderResponseDTO } from '../../../../core/models/api.models';

@Component({
  selector: 'app-manage-orders',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe, FormsModule],
  templateUrl: './manage-orders.component.html',
  styleUrl: './manage-orders.component.css'
})
export class ManageOrdersComponent implements OnInit {
  orders: OrderResponseDTO[] = [];
  filteredOrders: OrderResponseDTO[] = [];
  loading = false;
  errorMessage = '';
  searchTerm = '';
  selectedStatus = '';

  constructor(
    private orderService: OrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.errorMessage = '';

    this.orderService.getAllOrders().subscribe({
      next: (orders) => {
        this.orders = orders;
        this.filteredOrders = orders;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading orders:', error);
        if (error.status === 403) {
          this.errorMessage = 'You do not have permission to view orders.';
        } else {
          this.errorMessage = 'Failed to load orders. Please try again.';
        }
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  filterOrders(): void {
    this.filteredOrders = this.orders.filter(order => {
      const matchesSearch = !this.searchTerm || 
        order.id.toString().includes(this.searchTerm) ||
        order.customerId.toString().includes(this.searchTerm);

      return matchesSearch;
    });
  }

  getOrderStatus(order: OrderResponseDTO): string {
    // Since backend doesn't have status, return default
    return 'Processing';
  }

  getStatusClass(status: string): string {
    switch(status) {
      case 'Processing': return 'warning';
      case 'Shipped': return 'info';
      case 'Delivered': return 'success';
      case 'Cancelled': return 'danger';
      default: return 'secondary';
    }
  }
}

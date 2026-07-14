import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-order-item-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './order-item-list.component.html',
  styleUrl: './order-item-list.component.css'
})
export class OrderItemListComponent {
  @Input() items: any[] = [];
}

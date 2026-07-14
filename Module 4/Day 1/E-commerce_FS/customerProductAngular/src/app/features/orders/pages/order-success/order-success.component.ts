import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-order-success',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './order-success.component.html',
  styleUrl: './order-success.component.css'
})
export class OrderSuccessComponent {
  order = {
    id: 10025,
    date: new Date(),
    total: 2019.96,
    estimatedDelivery: '3-5 business days'
  };
}

import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoanProduct } from '../../../models/loan-product.model';

@Component({
  selector: 'app-loan-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './loan-card.component.html',
  styleUrls: ['./loan-card.component.css']
})
export class LoanCardComponent {
  @Input() loanProduct!: LoanProduct;
}

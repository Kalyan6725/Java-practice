import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-product-sort',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-sort.component.html',
  styleUrl: './product-sort.component.css'
})
export class ProductSortComponent implements OnInit {
  @Output() sortChanged = new EventEmitter<string>();
  
  ngOnInit(): void {
    console.log('🟡 [ProductSortComponent] ngOnInit called');
  }

  selectedSort = 'default';

  onSortChange(): void {
    this.sortChanged.emit(this.selectedSort);
  }
}

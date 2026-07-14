import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { ProductCardComponent } from '../../../../shared/components/product-card/product-card.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-grid',
  standalone: true,
  imports: [CommonModule, ProductCardComponent],
  templateUrl: './product-grid.component.html',
  styleUrl: './product-grid.component.css'
})
export class ProductGridComponent implements OnInit, OnChanges {
  @Input() products: any[] = [];
  
  ngOnInit(): void {
    console.log('🟢 [ProductGridComponent] ngOnInit - products:', this.products.length);
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['products']) {
      console.log('🟢 [ProductGridComponent] Products changed:', changes['products'].currentValue?.length);
      console.log('🟢 [ProductGridComponent] Products:', this.products);
    }
  }
}

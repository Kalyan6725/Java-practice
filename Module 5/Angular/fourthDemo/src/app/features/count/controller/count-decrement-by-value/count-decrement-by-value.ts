import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-count-decrement-by-value',
  imports: [FormsModule],
  templateUrl: './count-decrement-by-value.html',
  styleUrl: './count-decrement-by-value.css',
})
export class CountDecrementByValue {
  countService:CountService=inject(CountService);
  decrementValue:number=0;
}

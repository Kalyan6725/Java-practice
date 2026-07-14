import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-count-increment-by-value',
  imports: [FormsModule],
  templateUrl: './count-increment-by-value.html',
  styleUrl: './count-increment-by-value.css',
})
export class CountIncrementByValue {
  countService:CountService=inject(CountService);
  incrementValue:number=0;
}

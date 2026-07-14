import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';

@Component({
  selector: 'app-count-decrement',
  imports: [],
  templateUrl: './count-decrement.html',
  styleUrl: './count-decrement.css',
})
export class CountDecrement {
  countService:CountService= inject(CountService);
}

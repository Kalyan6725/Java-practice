import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';

@Component({
  selector: 'app-count-increment',
  imports: [],
  templateUrl: './count-increment.html',
  styleUrl: './count-increment.css',
})
export class CountIncrement {
  countService: CountService=inject(CountService);
}

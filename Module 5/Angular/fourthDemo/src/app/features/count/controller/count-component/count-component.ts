import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';
import { ShowCount } from '../show-count/show-count';
import { CountDecrement } from '../count-decrement/count-decrement';
import { CountIncrement } from '../count-increment/count-increment';
import { CountIncrementByValue } from '../count-increment-by-value/count-increment-by-value';
import { CountDecrementByValue } from '../count-decrement-by-value/count-decrement-by-value';

@Component({
  selector: 'app-count-component',
  imports: [ShowCount, CountIncrement, CountDecrement, CountIncrementByValue, CountDecrementByValue],
  templateUrl: './count-component.html',
  styleUrl: './count-component.css',
})
export class CountComponent {
  countService: CountService = inject(CountService);
}

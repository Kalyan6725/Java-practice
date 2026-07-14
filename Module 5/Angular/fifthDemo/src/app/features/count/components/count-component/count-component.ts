import { Component } from '@angular/core';
import { ShowCount } from '../show-count/show-count';
import { CountIncrement } from '../count-increment/count-increment';
import { CountDecrement } from '../count-decrement/count-decrement';

@Component({
  selector: 'app-count-component',
  imports: [ShowCount, CountIncrement, CountDecrement],
  templateUrl: './count-component.html',
  styleUrl: './count-component.css',
})
export class CountComponent {}

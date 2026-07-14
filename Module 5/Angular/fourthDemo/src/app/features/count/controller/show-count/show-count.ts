import { Component, inject } from '@angular/core';
import { CountService } from '../../service/count-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-show-count',
  imports: [AsyncPipe],
  templateUrl: './show-count.html',
  styleUrl: './show-count.css',
})
export class ShowCount {
  countService:CountService = inject(CountService);
}

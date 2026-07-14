import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stats-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stats-card.component.html',
  styleUrls: ['./stats-card.component.css']
})
export class StatsCardComponent {
  @Input() label: string = '';
  @Input() value: string | number = '';
  @Input() subValue?: string;
  @Input() icon: string = 'bi-bar-chart';
  @Input() iconColor: string = 'primary';
  @Input() change?: { value: string; positive: boolean };
}

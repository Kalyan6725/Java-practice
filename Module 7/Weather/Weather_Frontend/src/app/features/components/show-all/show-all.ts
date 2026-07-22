import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { WeatherService } from '../../services/weather-service';

@Component({
  selector: 'app-show-all',
  imports: [CommonModule, RouterLink],
  templateUrl: './show-all.html',
  styleUrl: './show-all.css',
})
export class ShowAll implements OnInit {
  private weatherService = inject(WeatherService);

  // shared signals from the service (single source of truth)
  weathers = this.weatherService.weathers;
  loading = this.weatherService.loading;
  error = this.weatherService.error;

  ngOnInit(): void {
    this.weatherService.load();
  }

  remove(id?: number): void {
    if (id == null) {
      return;
    }
    this.weatherService.delete(id).subscribe();
  }
}

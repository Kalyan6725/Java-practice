import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Weather, WeatherService } from '../../services/weather-service';

@Component({
  selector: 'app-show-component',
  imports: [CommonModule, RouterLink],
  templateUrl: './show-component.html',
  styleUrl: './show-component.css',
})
export class ShowComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private weatherService = inject(WeatherService);

  weather = signal<Weather | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.error.set('Invalid record id.');
      return;
    }
    this.load(id);
  }

  load(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.weatherService.getById(id).subscribe({
      next: (data) => {
        this.weather.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Weather record not found.');
        this.loading.set(false);
      },
    });
  }
}

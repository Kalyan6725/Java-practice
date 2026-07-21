import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HealthService } from '../../services/health.service';

@Component({
  selector: 'app-health',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './health.component.html'
})
export class HealthComponent {
  healthStatus = signal<'unknown' | 'up' | 'down'>('unknown');
  readyStatus = signal<'unknown' | 'up' | 'down'>('unknown');
  loading = signal(false);

  constructor(private healthService: HealthService) {}

  checkHealth(): void {
    this.loading.set(true);
    this.healthStatus.set('unknown');
    this.healthService.checkHealth().subscribe({
      next: () => {
        this.healthStatus.set('up');
        this.loading.set(false);
      },
      error: () => {
        this.healthStatus.set('down');
        this.loading.set(false);
      }
    });
  }

  checkReady(): void {
    this.loading.set(true);
    this.readyStatus.set('unknown');
    this.healthService.checkReady().subscribe({
      next: () => {
        this.readyStatus.set('up');
        this.loading.set(false);
      },
      error: () => {
        this.readyStatus.set('down');
        this.loading.set(false);
      }
    });
  }

  checkAll(): void {
    this.checkHealth();
    this.checkReady();
  }
}

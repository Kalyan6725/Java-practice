import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../services/scan.service';
import { Scan } from '../../models/scan.model';
import { LoadingComponent } from '../shared/loading/loading.component';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingComponent, EmptyStateComponent],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  totalScans = signal(0);
  recentScans = signal<Scan[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(private scanService: ScanService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading.set(true);
    this.scanService.getAllScans().subscribe({
      next: (scans) => {
        this.totalScans.set(scans.length);
        this.recentScans.set(scans.slice(-5).reverse());
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Failed to load dashboard data');
        this.loading.set(false);
      }
    });
  }
}

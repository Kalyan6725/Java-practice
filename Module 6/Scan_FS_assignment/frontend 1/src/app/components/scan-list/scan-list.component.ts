import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../services/scan.service';
import { Scan } from '../../models/scan.model';
import { LoadingComponent } from '../shared/loading/loading.component';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';

@Component({
  selector: 'app-scan-list',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingComponent, EmptyStateComponent],
  templateUrl: './scan-list.component.html'
})
export class ScanListComponent implements OnInit {
  scans = signal<Scan[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  constructor(private scanService: ScanService) {}

  ngOnInit(): void {
    this.loadScans();
  }

  loadScans(): void {
    this.loading.set(true);
    this.error.set(null);
    this.scanService.getAllScans().subscribe({
      next: (data) => {
        this.scans.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Failed to load scans');
        this.loading.set(false);
      }
    });
  }

  deleteScan(id: number): void {
    if (!confirm('Are you sure you want to delete this scan?')) return;

    this.scanService.deleteScan(id).subscribe({
      next: () => {
        this.successMessage.set('Scan deleted successfully');
        this.loadScans();
        setTimeout(() => this.successMessage.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Failed to delete scan');
      }
    });
  }
}

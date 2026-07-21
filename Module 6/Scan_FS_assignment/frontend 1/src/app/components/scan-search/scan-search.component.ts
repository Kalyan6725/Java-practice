import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../services/scan.service';
import { Scan } from '../../models/scan.model';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';

@Component({
  selector: 'app-scan-search',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, EmptyStateComponent],
  templateUrl: './scan-search.component.html'
})
export class ScanSearchComponent {
  domainName = '';
  orderBy = 'id';
  results = signal<Scan[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  searched = signal(false);

  orderByOptions = ['id', 'domainName', 'numPages', 'numBrokenLinks', 'numMissingImages', 'deleted'];

  constructor(private scanService: ScanService) {}

  onSearch(): void {
    if (!this.domainName.trim()) {
      this.error.set('Domain name is required');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.searched.set(true);

    this.scanService.searchScans(this.domainName.trim(), this.orderBy).subscribe({
      next: (data) => {
        this.results.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Search failed');
        this.loading.set(false);
        this.results.set([]);
      }
    });
  }
}

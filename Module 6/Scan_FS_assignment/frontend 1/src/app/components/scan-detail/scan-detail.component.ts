import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ScanService } from '../../services/scan.service';
import { Scan } from '../../models/scan.model';
import { LoadingComponent } from '../shared/loading/loading.component';

@Component({
  selector: 'app-scan-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingComponent],
  templateUrl: './scan-detail.component.html'
})
export class ScanDetailComponent implements OnInit {
  scan = signal<Scan | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private scanService: ScanService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadScan(id);
    }
  }

  loadScan(id: number): void {
    this.loading.set(true);
    this.error.set(null);
    this.scanService.getScanById(id).subscribe({
      next: (data) => {
        this.scan.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Scan not found');
        this.loading.set(false);
      }
    });
  }
}

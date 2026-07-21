import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ScanService } from '../../services/scan.service';
import { Scan } from '../../models/scan.model';

@Component({
  selector: 'app-scan-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './scan-create.component.html'
})
export class ScanCreateComponent {
  scanForm: FormGroup;
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private scanService: ScanService,
    private router: Router
  ) {
    this.scanForm = this.fb.group({
      domainName: ['', [Validators.required]],
      numPages: [0, [Validators.required, Validators.min(0)]],
      numBrokenLinks: [0, [Validators.required, Validators.min(0)]],
      numMissingImages: [0, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.scanForm.invalid) {
      this.scanForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    const newScan: Scan = this.scanForm.value;

    this.scanService.createScan(newScan).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/scans']);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Failed to create scan');
        this.loading.set(false);
      }
    });
  }

  get domainName() {
    return this.scanForm.get('domainName');
  }

  get numPages() {
    return this.scanForm.get('numPages');
  }

  get numBrokenLinks() {
    return this.scanForm.get('numBrokenLinks');
  }

  get numMissingImages() {
    return this.scanForm.get('numMissingImages');
  }
}

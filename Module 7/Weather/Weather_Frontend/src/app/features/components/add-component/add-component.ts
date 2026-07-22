import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Weather, WeatherService } from '../../services/weather-service';

@Component({
  selector: 'app-add-component',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './add-component.html',
  styleUrl: './add-component.css',
})
export class AddComponent {
  private fb = inject(FormBuilder);
  private weatherService = inject(WeatherService);
  private router = inject(Router);

  submitting = signal(false);
  error = signal<string | null>(null);

  form = this.fb.group({
    city: ['', [Validators.required, Validators.minLength(2)]],
    state: ['', [Validators.required, Validators.minLength(2)]],
    dateRecorded: ['', [Validators.required]],
    lat: [null as number | null, [Validators.required, Validators.min(-90), Validators.max(90)]],
    lon: [null as number | null, [Validators.required, Validators.min(-180), Validators.max(180)]],
    temperature: [null as number | null, [Validators.required]],
  });

  get f() {
    return this.form.controls;
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.error.set(null);

    this.weatherService.create(this.form.value as Weather).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => {
        this.error.set('Failed to save record. Please try again.');
        this.submitting.set(false);
      },
    });
  }
}

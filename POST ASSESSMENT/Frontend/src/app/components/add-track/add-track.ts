import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TrackService } from '../../services/track';
import { TrackRequest } from '../../models/track';

@Component({
  selector: 'app-add-track',
  imports: [FormsModule],
  templateUrl: './add-track.html',
  styleUrl: './add-track.css',
})
export class AddTrack {
  private readonly trackService = inject(TrackService);
  private readonly router = inject(Router);

  model: TrackRequest = { title: '', albumName: '', releaseDate: '', playCount: 0 };
  readonly error = signal<string | null>(null);

  onSubmit(): void {
    this.error.set(null);
    this.trackService.createTrack(this.model).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => this.error.set('Failed to create track.'),
    });
  }
}

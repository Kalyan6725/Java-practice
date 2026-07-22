import { Component, OnInit, inject, signal } from '@angular/core';
import { TrackService } from '../../services/track';
import { Track } from '../../models/track';

@Component({
  selector: 'app-show-all',
  imports: [],
  templateUrl: './show-all.html',
  styleUrl: './show-all.css',
})
export class ShowAll implements OnInit {
  private readonly trackService = inject(TrackService);

  readonly tracks = signal<Track[]>([]);
  readonly error = signal<string | null>(null);
  readonly loading = signal<boolean>(false);

  ngOnInit(): void {
    this.loadTracks();
  }

  loadTracks(): void {
    this.loading.set(true);
    this.error.set(null);
    this.trackService.getAllTracks().subscribe({
      next: (data) => {
        this.tracks.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load tracks.');
        this.loading.set(false);
      },
    });
  }

  deleteTrack(id: number): void {
    this.trackService.deleteTrack(id).subscribe({
      next: () => this.tracks.update((list) => list.filter((t) => t.id !== id)),
      error: () => this.error.set('Failed to delete track.'),
    });
  }
}

import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TrackService } from '../../services/track';
import { Track } from '../../models/track';

@Component({
  selector: 'app-search',
  imports: [],
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {
  private readonly route = inject(ActivatedRoute);
  private readonly trackService = inject(TrackService);

  readonly track = signal<Track | null>(null);
  readonly error = signal<string | null>(null);
  readonly query = signal<string>('');

  constructor() {
    this.route.queryParams.subscribe((params) => {
      const title = params['title'];
      if (title) {
        this.query.set(title);
        this.search(title);
      }
    });
  }

  search(title: string): void {
    this.error.set(null);
    this.track.set(null);
    this.trackService.searchByTitle(title).subscribe({
      next: (data) => this.track.set(data),
      error: () => this.error.set(`No track found for "${title}".`),
    });
  }
}

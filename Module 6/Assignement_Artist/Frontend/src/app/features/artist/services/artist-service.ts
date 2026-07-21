import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Artist, ArtistRequest } from '../models/artist.model';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {
  private apiUrl = 'http://localhost:9090/v1/artists';
  artists = signal<Artist[]>([]);

  constructor(private http: HttpClient) {
    this.loadArtists();
  }

  loadArtists() {
    this.http.get<Artist[]>(this.apiUrl).subscribe({
      next: (data) => this.artists.set(data),
      error: (err) => console.error('Error loading artists:', err)
    });
  }

  createArtist(request: ArtistRequest) {
    this.http.post<Artist>(this.apiUrl, request).subscribe({
      next: (artist) => {
        this.artists.update(artists => [...artists, artist]);
      },
      error: (err) => console.error('Error creating artist:', err)
    });
  }

  getArtistById(id: number) {
    return this.http.get<Artist>(`${this.apiUrl}/${id}`);
  }

  deleteArtist(id: number) {
    this.http.delete<void>(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        this.artists.update(artists => artists.filter(a => a.id !== id));
      },
      error: (err) => console.error('Error deleting artist:', err)
    });
  }
}

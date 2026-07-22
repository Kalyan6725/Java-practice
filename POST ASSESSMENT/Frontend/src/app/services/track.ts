import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Track, TrackRequest } from '../models/track';

@Injectable({ providedIn: 'root' })
export class TrackService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/music/platform/v1/tracks';

  getAllTracks(): Observable<Track[]> {
    return this.http.get<Track[]>(this.baseUrl);
  }

  createTrack(request: TrackRequest): Observable<Track> {
    return this.http.post<Track>(this.baseUrl, request);
  }

  deleteTrack(trackId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${trackId}`);
  }

  searchByTitle(title: string): Observable<Track> {
    return this.http.get<Track>(`${this.baseUrl}/search`, { params: { title } });
  }
}

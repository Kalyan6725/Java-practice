import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

export interface Weather {
  id?: number;
  dateRecorded: string;
  lat: number;
  lon: number;
  city: string;
  state: string;
  temperature: number;
}

@Injectable({ providedIn: 'root' })
export class WeatherService {
  private http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:9090/weather';

  // --- shared state (single source of truth) ---
  private readonly _weathers = signal<Weather[]>([]);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  // public read-only views
  readonly weathers = this._weathers.asReadonly();
  readonly loading = this._loading.asReadonly();
  readonly error = this._error.asReadonly();
  readonly count = computed(() => this._weathers().length);

  /** Load all records into the shared signal. */
  load(): void {
    this._loading.set(true);
    this._error.set(null);
    this.http.get<Weather[]>(this.baseUrl).subscribe({
      next: (data) => {
        this._weathers.set(data);
        this._loading.set(false);
      },
      error: () => {
        this._error.set('Failed to load weather data. Is the backend running?');
        this._loading.set(false);
      },
    });
  }

  getById(id: number): Observable<Weather> {
    return this.http.get<Weather>(`${this.baseUrl}/${id}`);
  }

  /** Create a record and append it to the shared signal on success. */
  create(weather: Weather): Observable<Weather> {
    return this.http
      .post<Weather>(this.baseUrl, weather)
      .pipe(tap((saved) => this._weathers.update((list) => [...list, saved])));
  }

  /** Update a record and replace it in the shared signal on success. */
  update(id: number, weather: Weather): Observable<Weather> {
    return this.http
      .put<Weather>(`${this.baseUrl}/${id}`, weather)
      .pipe(tap((saved) => this._weathers.update((list) => list.map((w) => (w.id === id ? saved : w)))));
  }

  /** Delete a record and remove it from the shared signal on success. */
  delete(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(tap(() => this._weathers.update((list) => list.filter((w) => w.id !== id))));
  }
}

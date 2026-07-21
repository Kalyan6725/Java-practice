import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HealthService {
  private readonly baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  checkHealth(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}/health`);
  }

  checkReady(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}/ready`);
  }
}

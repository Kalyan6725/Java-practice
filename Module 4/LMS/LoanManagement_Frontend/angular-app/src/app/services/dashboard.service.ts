import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { Dashboard } from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly baseUrl = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  /** MANAGER/ADMIN: portfolio metrics. */
  getDashboard(): Observable<Dashboard> {
    return this.http
      .get<ApiResponse<Dashboard>>(this.baseUrl)
      .pipe(map((r) => r.data));
  }
}

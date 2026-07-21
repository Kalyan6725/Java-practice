import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Scan } from '../models/scan.model';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ScanService {
  private readonly apiUrl = `${environment.apiBaseUrl}/scan`;

  constructor(private http: HttpClient) {}

  getAllScans(): Observable<Scan[]> {
    return this.http.get<Scan[]>(this.apiUrl);
  }

  getScanById(id: number): Observable<Scan> {
    return this.http.get<Scan>(`${this.apiUrl}/${id}`);
  }

  createScan(scan: Scan): Observable<Scan> {
    return this.http.post<Scan>(this.apiUrl, scan);
  }

  deleteScan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchScans(domainName: string, orderBy: string): Observable<Scan[]> {
    const params = new HttpParams().set('orderBy', orderBy);
    return this.http.get<Scan[]>(`${this.apiUrl}/search/${encodeURIComponent(domainName)}`, { params });
  }
}

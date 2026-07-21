import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import {
  LoanApplication,
  LoanApplicationRequest,
  LoanApplicationReviewRequest,
} from '../models/loan-application.model';

@Injectable({ providedIn: 'root' })
export class LoanApplicationService {
  private readonly baseUrl = `${environment.apiUrl}/loan-applications`;

  constructor(private http: HttpClient) {}

  apply(payload: LoanApplicationRequest): Observable<LoanApplication> {
    return this.http
      .post<ApiResponse<LoanApplication>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  /** Current customer's own applications. */
  getMine(): Observable<LoanApplication[]> {
    return this.http
      .get<ApiResponse<LoanApplication[]>>(`${this.baseUrl}/my`)
      .pipe(map((r) => r.data));
  }

  getById(applicationId: number): Observable<LoanApplication> {
    return this.http
      .get<ApiResponse<LoanApplication>>(`${this.baseUrl}/${applicationId}`)
      .pipe(map((r) => r.data));
  }

  /** Staff: all applications, optionally filtered by status. */
  getAll(status?: string): Observable<LoanApplication[]> {
    const url = status ? `${this.baseUrl}?status=${status}` : this.baseUrl;
    return this.http
      .get<ApiResponse<LoanApplication[]>>(url)
      .pipe(map((r) => r.data));
  }

  /** Staff: pending review queue. */
  getPending(): Observable<LoanApplication[]> {
    return this.http
      .get<ApiResponse<LoanApplication[]>>(`${this.baseUrl}/pending`)
      .pipe(map((r) => r.data));
  }

  getByCustomer(customerId: number): Observable<LoanApplication[]> {
    return this.http
      .get<ApiResponse<LoanApplication[]>>(`${this.baseUrl}/customer/${customerId}`)
      .pipe(map((r) => r.data));
  }

  /** UNDERWRITER/ADMIN: approve, reject or mark under review. */
  review(applicationId: number, payload: LoanApplicationReviewRequest): Observable<LoanApplication> {
    return this.http
      .put<ApiResponse<LoanApplication>>(`${this.baseUrl}/${applicationId}/review`, payload)
      .pipe(map((r) => r.data));
  }
}

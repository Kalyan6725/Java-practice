import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { EmiPayment, EmiPaymentRequest } from '../models/emi-payment.model';

@Injectable({ providedIn: 'root' })
export class EmiPaymentService {
  private readonly baseUrl = `${environment.apiUrl}/emi-payments`;

  constructor(private http: HttpClient) {}

  pay(payload: EmiPaymentRequest): Observable<EmiPayment> {
    return this.http
      .post<ApiResponse<EmiPayment>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  getById(paymentId: number): Observable<EmiPayment> {
    return this.http
      .get<ApiResponse<EmiPayment>>(`${this.baseUrl}/${paymentId}`)
      .pipe(map((r) => r.data));
  }

  /** MANAGER/ADMIN: all payments. */
  getAll(): Observable<EmiPayment[]> {
    return this.http
      .get<ApiResponse<EmiPayment[]>>(this.baseUrl)
      .pipe(map((r) => r.data));
  }

  getByAccount(loanAccountId: number): Observable<EmiPayment[]> {
    return this.http
      .get<ApiResponse<EmiPayment[]>>(`${this.baseUrl}/account/${loanAccountId}`)
      .pipe(map((r) => r.data));
  }

  /** Full EMI schedule for an account. */
  getSchedule(loanAccountId: number): Observable<EmiPayment[]> {
    return this.http
      .get<ApiResponse<EmiPayment[]>>(`${this.baseUrl}/account/${loanAccountId}/schedule`)
      .pipe(map((r) => r.data));
  }
}

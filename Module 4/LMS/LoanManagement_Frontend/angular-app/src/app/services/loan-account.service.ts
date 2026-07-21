import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { LoanAccount } from '../models/loan-account.model';

@Injectable({ providedIn: 'root' })
export class LoanAccountService {
  private readonly baseUrl = `${environment.apiUrl}/loan-accounts`;

  constructor(private http: HttpClient) {}

  /** Current customer's own accounts. */
  getMyAccounts(): Observable<LoanAccount[]> {
    return this.http
      .get<ApiResponse<LoanAccount[]>>(`${this.baseUrl}/my`)
      .pipe(map((r) => r.data));
  }

  getById(loanAccountId: number): Observable<LoanAccount> {
    return this.http
      .get<ApiResponse<LoanAccount>>(`${this.baseUrl}/${loanAccountId}`)
      .pipe(map((r) => r.data));
  }

  /** MANAGER/ADMIN: all accounts. */
  getAll(): Observable<LoanAccount[]> {
    return this.http
      .get<ApiResponse<LoanAccount[]>>(this.baseUrl)
      .pipe(map((r) => r.data));
  }

  getByCustomer(customerId: number): Observable<LoanAccount[]> {
    return this.http
      .get<ApiResponse<LoanAccount[]>>(`${this.baseUrl}/customer/${customerId}`)
      .pipe(map((r) => r.data));
  }

  getByStatus(status: string): Observable<LoanAccount[]> {
    return this.http
      .get<ApiResponse<LoanAccount[]>>(`${this.baseUrl}/status/${status}`)
      .pipe(map((r) => r.data));
  }

  /** MANAGER/ADMIN: disburse an approved account. */
  disburse(loanAccountId: number): Observable<LoanAccount> {
    return this.http
      .post<ApiResponse<LoanAccount>>(`${this.baseUrl}/${loanAccountId}/disburse`, {})
      .pipe(map((r) => r.data));
  }
}

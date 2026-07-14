import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Loan } from '../models/loan.model';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoanAccountService {
  private apiUrl = 'http://localhost:8080/api/loan';

  constructor(private http: HttpClient) { }

  createLoanAccount(loanAccount: any): Observable<ApiResponse<Loan>> {
    return this.http.post<ApiResponse<Loan>>(`${this.apiUrl}/account/create`, loanAccount);
  }

  getLoanAccountById(loanAccountId: number): Observable<ApiResponse<Loan>> {
    return this.http.get<ApiResponse<Loan>>(`${this.apiUrl}/account/${loanAccountId}`);
  }

  getAllLoanAccounts(): Observable<ApiResponse<Loan[]>> {
    return this.http.get<ApiResponse<Loan[]>>(`${this.apiUrl}/accounts`);
  }

  getLoanAccountsByCustomerId(customerId: number): Observable<ApiResponse<Loan[]>> {
    return this.http.get<ApiResponse<Loan[]>>(`${this.apiUrl}/accounts/customer/${customerId}`);
  }

  getLoanAccountsByStatus(status: string): Observable<ApiResponse<Loan[]>> {
    return this.http.get<ApiResponse<Loan[]>>(`${this.apiUrl}/accounts/status/${status}`);
  }

  updateLoanAccount(loanAccount: Loan): Observable<ApiResponse<Loan>> {
    return this.http.put<ApiResponse<Loan>>(`${this.apiUrl}/account/update`, loanAccount);
  }

  deleteLoanAccount(loanAccountId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/account/${loanAccountId}`);
  }
}

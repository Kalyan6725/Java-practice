import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EMIPayment } from '../models/emi-payment.model';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmiPaymentService {
  private apiUrl = 'http://localhost:8080/api/loan';

  constructor(private http: HttpClient) { }

  recordEmiPayment(emiPayment: EMIPayment): Observable<ApiResponse<EMIPayment>> {
    return this.http.post<ApiResponse<EMIPayment>>(`${this.apiUrl}/payment/record`, emiPayment);
  }

  getEmiPaymentById(paymentId: number): Observable<ApiResponse<EMIPayment>> {
    return this.http.get<ApiResponse<EMIPayment>>(`${this.apiUrl}/payment/${paymentId}`);
  }

  getAllEmiPayments(): Observable<ApiResponse<EMIPayment[]>> {
    return this.http.get<ApiResponse<EMIPayment[]>>(`${this.apiUrl}/payments`);
  }

  getEmiPaymentsByLoanAccountId(loanAccountId: number): Observable<ApiResponse<EMIPayment[]>> {
    return this.http.get<ApiResponse<EMIPayment[]>>(`${this.apiUrl}/payments/account/${loanAccountId}`);
  }

  getEmiPaymentsByPaymentType(paymentType: string): Observable<ApiResponse<EMIPayment[]>> {
    return this.http.get<ApiResponse<EMIPayment[]>>(`${this.apiUrl}/payments/type/${paymentType}`);
  }

  updateEmiPayment(emiPayment: EMIPayment): Observable<ApiResponse<EMIPayment>> {
    return this.http.put<ApiResponse<EMIPayment>>(`${this.apiUrl}/payment/update`, emiPayment);
  }

  deleteEmiPayment(paymentId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/payment/${paymentId}`);
  }
}

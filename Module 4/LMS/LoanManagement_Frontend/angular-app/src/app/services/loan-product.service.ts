import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse, PagedData } from '../models/api-response.model';
import { LoanProduct, LoanProductRequest } from '../models/loan-product.model';

@Injectable({ providedIn: 'root' })
export class LoanProductService {
  private readonly baseUrl = `${environment.apiUrl}/loan-products`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<LoanProduct[]> {
    return this.http
      .get<ApiResponse<LoanProduct[]>>(this.baseUrl)
      .pipe(map((r) => r.data));
  }

  getActive(): Observable<LoanProduct[]> {
    return this.http
      .get<ApiResponse<LoanProduct[]>>(`${this.baseUrl}/active`)
      .pipe(map((r) => r.data));
  }

  getByCode(loanCode: string): Observable<LoanProduct> {
    return this.http
      .get<ApiResponse<LoanProduct>>(`${this.baseUrl}/${loanCode}`)
      .pipe(map((r) => r.data));
  }

  getPaged(page = 0, size = 10, sortBy = 'dailyPenaltyRate', direction: 'ASC' | 'DESC' = 'DESC'): Observable<PagedData<LoanProduct>> {
    const params = `?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`;
    return this.http
      .get<ApiResponse<PagedData<LoanProduct>>>(`${this.baseUrl}/paged${params}`)
      .pipe(map((r) => r.data));
  }

  create(payload: LoanProductRequest): Observable<LoanProduct> {
    return this.http
      .post<ApiResponse<LoanProduct>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  update(payload: LoanProductRequest): Observable<LoanProduct> {
    return this.http
      .put<ApiResponse<LoanProduct>>(this.baseUrl, payload)
      .pipe(map((r) => r.data));
  }

  delete(loanCode: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${loanCode}`)
      .pipe(map((r) => r.data));
  }
}

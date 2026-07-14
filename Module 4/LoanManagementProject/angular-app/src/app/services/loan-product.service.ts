import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanProduct } from '../models/loan-product.model';

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}

interface PageMeta {
  number: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

interface PagedData<T> {
  items: T[];
  page: PageMeta;
}

@Injectable({
  providedIn: 'root'
})
export class LoanProductService {
  private apiUrl = 'http://localhost:8080/api/loan';

  constructor(private http: HttpClient) { }

  createLoanProduct(loanProduct: LoanProduct): Observable<ApiResponse<LoanProduct>> {
    return this.http.post<ApiResponse<LoanProduct>>(`${this.apiUrl}/product/create`, loanProduct);
  }

  getLoanProductByCode(loanCode: string): Observable<ApiResponse<LoanProduct>> {
    return this.http.get<ApiResponse<LoanProduct>>(`${this.apiUrl}/product/${loanCode}`);
  }

  getAllLoanProducts(): Observable<ApiResponse<LoanProduct[]>> {
    return this.http.get<ApiResponse<LoanProduct[]>>(`${this.apiUrl}/products`);
  }

  getLoanProductsWithPagination(
    page: number = 0,
    size: number = 10,
    sort: string = 'dailyPenaltyRate'
  ): Observable<ApiResponse<PagedData<LoanProduct>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<ApiResponse<PagedData<LoanProduct>>>(`${this.apiUrl}/loan-products`, { params });
  }

  searchLoanProducts(
    searchParams: {
      loanType?: string;
      loanName?: string;
      minPenalty?: number;
      maxPenalty?: number;
      page?: number;
      size?: number;
      sortBy?: string;
      direction?: 'ASC' | 'DESC';
    }
  ): Observable<ApiResponse<PagedData<LoanProduct>>> {
    let params = new HttpParams();
    
    if (searchParams.loanType) params = params.set('loanType', searchParams.loanType);
    if (searchParams.loanName) params = params.set('loanName', searchParams.loanName);
    if (searchParams.minPenalty !== undefined) params = params.set('minPenalty', searchParams.minPenalty.toString());
    if (searchParams.maxPenalty !== undefined) params = params.set('maxPenalty', searchParams.maxPenalty.toString());
    params = params.set('page', (searchParams.page || 0).toString());
    params = params.set('size', (searchParams.size || 10).toString());
    params = params.set('sortBy', searchParams.sortBy || 'dailyPenaltyRate');
    params = params.set('direction', searchParams.direction || 'DESC');

    return this.http.get<ApiResponse<PagedData<LoanProduct>>>(`${this.apiUrl}/loan-products/search`, { params });
  }

  updateLoanProduct(loanProduct: LoanProduct): Observable<ApiResponse<LoanProduct>> {
    return this.http.put<ApiResponse<LoanProduct>>(`${this.apiUrl}/product/update`, loanProduct);
  }

  deleteLoanProduct(loanCode: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/product/${loanCode}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProductRequestDTO, ProductResponseDTO } from '../models/api.models';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly API_URL = environment.apiUrl;

  constructor(private http: HttpClient) { }

  /**
   * Get all products
   * Endpoint: GET /api/products
   * Access: USER, ADMIN
   */
  getAllProducts(): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.base}`;
    console.log('🟢 [ProductService] getAllProducts() called');
    console.log('🟢 [ProductService] API URL:', this.API_URL);
    console.log('🟢 [ProductService] Full endpoint:', url);
    console.log('🟢 [ProductService] Making HTTP GET request...');
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get product by ID
   * Endpoint: GET /api/products/{id}
   * Access: USER, ADMIN
   */
  getProductById(id: number): Observable<ProductResponseDTO> {
    const url = `${this.API_URL}/api/products/${id}`;
    return this.http.get<ProductResponseDTO>(url);
  }

  /**
   * Create new product
   * Endpoint: POST /api/products
   * Access: ADMIN only
   */
  createProduct(product: ProductRequestDTO): Observable<ProductResponseDTO> {
    const url = `${this.API_URL}${environment.endpoints.products.base}`;
    return this.http.post<ProductResponseDTO>(url, product);
  }

  /**
   * Update existing product
   * Endpoint: PUT /api/products/{id}
   * Access: ADMIN only
   */
  updateProduct(id: number, product: ProductRequestDTO): Observable<ProductResponseDTO> {
    const url = `${this.API_URL}/api/products/${id}`;
    return this.http.put<ProductResponseDTO>(url, product);
  }

  /**
   * Delete product
   * Endpoint: DELETE /api/products/{id}
   * Access: ADMIN only
   */
  deleteProduct(id: number): Observable<string> {
    const url = `${this.API_URL}/api/products/${id}`;
    return this.http.delete(url, { responseType: 'text' });
  }

  /**
   * Get products by category
   * Endpoint: GET /api/products/category/{category}
   * Access: USER, ADMIN
   */
  getProductsByCategory(category: string): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}/api/products/category/${category}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get products by brand
   * Endpoint: GET /api/products/brand/{brand}
   * Access: USER, ADMIN
   */
  getProductsByBrand(brand: string): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}/api/products/brand/${brand}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get products by price range
   * Endpoint: GET /api/products/price-range?minPrice={min}&maxPrice={max}
   * Access: USER, ADMIN
   */
  getProductsByPriceRange(minPrice: number, maxPrice: number): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.byPriceRange}`;
    const params = new HttpParams()
      .set('minPrice', minPrice.toString())
      .set('maxPrice', maxPrice.toString());
    
    return this.http.get<ProductResponseDTO[]>(url, { params });
  }

  /**
   * Get products sorted by price (ascending)
   * Endpoint: GET /api/products/sort/price-asc
   * Access: USER, ADMIN
   */
  sortProductsByPriceAsc(): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.sortByPriceAsc}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get products sorted by price (descending)
   * Endpoint: GET /api/products/sort/price-desc
   * Access: USER, ADMIN
   */
  sortProductsByPriceDesc(): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.sortByPriceDesc}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get products sorted by name
   * Endpoint: GET /api/products/sort/name
   * Access: USER, ADMIN
   */
  sortProductsByName(): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.sortByName}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Get products sorted by category
   * Endpoint: GET /api/products/sort/category
   * Access: USER, ADMIN
   */
  sortProductsByCategory(): Observable<ProductResponseDTO[]> {
    const url = `${this.API_URL}${environment.endpoints.products.sortByCategory}`;
    return this.http.get<ProductResponseDTO[]>(url);
  }

  /**
   * Advanced filter and sort (client-side for now, can be moved to backend)
   */
  filterAndSortProducts(
    products: ProductResponseDTO[],
    filters: {
      categories?: string[];
      brands?: string[];
      minPrice?: number;
      maxPrice?: number;
      inStockOnly?: boolean;
    }
  ): ProductResponseDTO[] {
    let filtered = [...products];

    // Filter by categories
    if (filters.categories && filters.categories.length > 0) {
      filtered = filtered.filter(p => filters.categories!.includes(p.category));
    }

    // Filter by brands
    if (filters.brands && filters.brands.length > 0) {
      filtered = filtered.filter(p => filters.brands!.includes(p.brand));
    }

    // Filter by price range
    if (filters.minPrice !== undefined) {
      filtered = filtered.filter(p => p.price >= filters.minPrice!);
    }
    if (filters.maxPrice !== undefined) {
      filtered = filtered.filter(p => p.price <= filters.maxPrice!);
    }

    // Filter by stock availability
    if (filters.inStockOnly) {
      filtered = filtered.filter(p => p.stock > 0);
    }

    return filtered;
  }
}

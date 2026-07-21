/** Standard response envelope returned by every backend endpoint. */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string | null;
}

/** Paged payload used by /loan-products/paged and /search. */
export interface PagedData<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private requestCount = 0;

  public loading$: Observable<boolean> = this.loadingSubject.asObservable();

  constructor() { }

  /**
   * Show loading indicator
   */
  show(): void {
    this.requestCount++;
    console.log('⏳ [LoadingService] show() called - Request count:', this.requestCount);
    if (!this.loadingSubject.value) {
      this.loadingSubject.next(true);
      console.log('⏳ [LoadingService] Loading state set to TRUE');
    }
  }

  /**
   * Hide loading indicator
   */
  hide(): void {
    this.requestCount--;
    console.log('⏳ [LoadingService] hide() called - Request count:', this.requestCount);
    if (this.requestCount <= 0) {
      this.requestCount = 0;
      this.loadingSubject.next(false);
      console.log('⏳ [LoadingService] Loading state set to FALSE');
    }
  }

  /**
   * Get current loading state
   */
  isLoading(): boolean {
    return this.loadingSubject.value;
  }
}

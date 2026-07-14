import { Injectable, Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CountService {
    private count$ = new BehaviorSubject<number>(0);
    increment(): void {
        
        this.count$.next(this.count$.value + 1);
        console.log(this.count$.value);
    }

    decrement(): void {
        
        this.count$.next(this.count$.value - 1);
        console.log(this.count$.value);
    }

    getCount(): BehaviorSubject<number> {
        console.log(this.count$.value);
        return this.count$;
        
    }
    incrementBy(value: number): void {
        this.count$.next(this.count$.value + value);
    }

    decrementBy(value: number): void {
        this.count$.next(this.count$.value - value);
    }
    
}

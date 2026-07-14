import { Service, signal, WritableSignal } from '@angular/core';

@Service()
export class CountService {
    private count :WritableSignal<number> = signal(0);

    getCount() :WritableSignal<number> {
        return this.count;
    }

    increment() :void {
        this.count.set(this.count() + 1);
    }

    decrement() :void {
        this.count.set(this.count() - 1);
    }
}

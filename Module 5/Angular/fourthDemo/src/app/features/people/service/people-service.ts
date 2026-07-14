import { Service } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Service()
export class PeopleService {
    private names$: BehaviorSubject<string[]> = new BehaviorSubject<string[]>(['Alice', 'Bob', 'Charlie']);

    getNames(): BehaviorSubject<string[]> {
        return this.names$;
    }

    addName(name: string): void {
        const currentNames = this.names$.getValue();
        this.names$.next([...currentNames, name]);
    }

    deleteName(name: string): void {
        const currentNames = this.names$.getValue();
        const filteredNames = currentNames.filter(n => n !== name);
        this.names$.next(filteredNames);
    }

    updateName(oldName: string, newName: string): void {
        const currentNames = this.names$.getValue();
        const index = currentNames.indexOf(oldName);
        if (index > -1) {
            currentNames[index] = newName;
            this.names$.next([...currentNames]);
        }
    }
}

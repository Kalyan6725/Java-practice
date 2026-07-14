import { Service, signal, WritableSignal } from '@angular/core';

@Service()
export class PeopleService {
    private names: WritableSignal<string[]> = signal(['Alice', 'Bob', 'Charlie']);

    getNames(): WritableSignal<string[]> {
        return this.names;
    }

    addName(name: string): void {
        const currentNames = this.names();
        this.names.set([...currentNames, name]);
    }

    deleteName(name: string): void {
        const currentNames = this.names();
        const filteredNames = currentNames.filter(n => n !== name);
        this.names.set(filteredNames);
    }

    updateName(oldName: string, newName: string): void {
        const currentNames = this.names();
        const index = currentNames.indexOf(oldName);
        if (index > -1) {
            currentNames[index] = newName;
            this.names.set([...currentNames]);
        }
    }
}

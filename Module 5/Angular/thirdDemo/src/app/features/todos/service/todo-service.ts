import { Injectable } from '@angular/core';
import TodoDTO from '../../../dto/TodoDTO';

@Injectable({
    providedIn: 'root'
})
export class TodoService {
    private todos: TodoDTO[] = [
        {
            id: 1,
            title: 'Todo1',
            description: 'This is a first todo item',
            completed: false
        },
        {
            id: 2,
            title: 'Todo2',
            description: 'This is second todo item',
            completed: true
        },
        {
            id: 3,
            title: 'Todo3',
            description: 'This is the third todo item',
            completed: false
        }
    ];
    public getTodos(): TodoDTO[] {
        return this.todos;
    }
    public addTodo(todo: TodoDTO): void {
        this.todos.push(todo);
    }
    public updateTodo(updatedTodo: TodoDTO): void {
        const index = this.todos.findIndex(todo => todo.id === updatedTodo.id);
        if (index !== -1) {
            this.todos[index] = updatedTodo;
        }
    }
    public deleteTodo(id: number): void {
        this.todos = this.todos.filter(todo => todo.id !== id);
    }
    public markAsDone(id: number): void {
        const todo = this.todos.find(todo => todo.id === id);
        if (todo) {
            todo.completed = true;
        }
    }
}

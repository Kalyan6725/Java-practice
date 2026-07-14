import { Component, inject } from '@angular/core';
import { TodoService } from '../../service/todo-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-todo',
  imports: [FormsModule],
  templateUrl: './update-todo.html',
  styleUrl: './update-todo.css',
})
export class UpdateTodo {
  todoService: TodoService=inject(TodoService);
  updatedTodo = {
    id: 0,
    title: '',
    description: '',
    completed: false
  };
  updateTodo() {
    if (this.updatedTodo.title.trim() !== '' && this.updatedTodo.description.trim() !== '' && this.updatedTodo.id > 0) {
      this.todoService.updateTodo({...this.updatedTodo});
    }
    this.updatedTodo = {
      id: 0,
      title: '',
      description: '',
      completed: false
     }
    }
}

import { Component, inject } from '@angular/core';
import TodoDTO from '../../../../dto/TodoDTO';
import { TodoService } from '../../service/todo-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-todo',
  imports: [FormsModule],
  templateUrl: './add-todo.html',
  styleUrl: './add-todo.css',
})
export class AddTodo {

  todoService: TodoService=inject(TodoService);
  newTodo:TodoDTO ={
    id: 0,
    title: '',
    description: '',
    completed: false
  }

  addTodo() {
    if (this.newTodo.title.trim() !== '' && this.newTodo.description.trim() !== '' && this.newTodo.id > 0) {
      this.todoService.addTodo({...this.newTodo});
    }
    this.newTodo = {
      id: 0,
      title: '',
      description: '',
      completed: false
     }
    }

}

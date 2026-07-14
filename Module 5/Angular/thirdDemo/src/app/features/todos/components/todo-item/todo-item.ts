import { Component, inject, Input } from '@angular/core';
import TodoDTO from '../../../../dto/TodoDTO';
import { TodoService } from '../../service/todo-service';

@Component({
  selector: 'tr[app-todo-item]',
  imports: [],
  templateUrl: './todo-item.html',
  styleUrl: './todo-item.css',
})
export class TodoItem {
  @Input()
  todo!: TodoDTO;

  todoService: TodoService=inject(TodoService);
}

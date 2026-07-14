import { Component, inject } from '@angular/core';
import { TodoService } from '../../service/todo-service';
import { TodoItem } from '../todo-item/todo-item';
import { AddTodo } from '../add-todo/add-todo';
import { UpdateTodo } from '../update-todo/update-todo';

@Component({
  selector: 'app-todo-component',
  imports: [TodoItem,AddTodo,UpdateTodo],
  templateUrl: './todo-component.html',
  styleUrl: './todo-component.css',
})
export class TodoComponent {
  todoService: TodoService=inject(TodoService);

}

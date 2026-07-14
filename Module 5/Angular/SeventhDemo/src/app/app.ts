import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsComponent } from './forms-component/forms-component';
import { ReactiveFormsComponent } from './reactive-forms-component/reactive-forms-component';
import { Pipes } from "./pipes/pipes";

@Component({
  selector: 'app-root',
  imports: [FormsComponent, ReactiveFormsComponent, Pipes],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('SeventhDemo');
}

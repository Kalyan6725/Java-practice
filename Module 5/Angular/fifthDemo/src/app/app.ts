import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CountComponent } from './features/count/components/count-component/count-component';
import { PeopleComponent } from './features/people/controller/people-component/people-component';
import { MoviesComponent } from './features/movies/components/movies-component/movies-component';

@Component({
  selector: 'app-root',
  imports: [CountComponent,PeopleComponent,MoviesComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('fifthDemo');
}

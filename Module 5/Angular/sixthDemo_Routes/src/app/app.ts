import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './features/navbar/components/navbar-component/navbar-component';

@Component({
  selector: 'app-root',
  imports: [NavbarComponent,RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('sixthDemo_Routes');
}

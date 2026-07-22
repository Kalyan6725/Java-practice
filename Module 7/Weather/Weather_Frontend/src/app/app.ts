import { Component, signal } from '@angular/core';
import { MainComponent } from './features/components/main-component/main-component';


@Component({
  selector: 'app-root',
  imports: [MainComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Weather_Frontend');
}

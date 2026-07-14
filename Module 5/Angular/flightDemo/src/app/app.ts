import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FlightComponent } from './features/flight/components/flight-component/flight-component';

@Component({
  selector: 'app-root',
  imports: [FlightComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('flightDemo');
}

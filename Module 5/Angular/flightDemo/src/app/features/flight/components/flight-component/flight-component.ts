import { Component, inject } from '@angular/core';
import { FlightService } from '../../service/flight-service';

@Component({
  selector: 'app-flight-component',
  imports: [],
  templateUrl: './flight-component.html',
  styleUrl: './flight-component.css',
})
export class FlightComponent {
  flightService: FlightService=inject(FlightService);
}

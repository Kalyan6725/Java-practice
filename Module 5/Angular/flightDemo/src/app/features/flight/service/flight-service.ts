import { Service } from '@angular/core';
import { FlightDTO } from '../../../dto/FlightDTO';

@Service()
export class FlightService {
    flights: FlightDTO[] = [
        {
            id: 1,
            flightNumber: 'AI101',
            departure: 'New York',
            arrival: 'London',
            departureTime: new Date('2024-06-01T10:00:00'),
            arrivalTime: new Date('2024-06-01T20:00:00'),
            price: 500
        },
        {
            id: 2,
            flightNumber: 'BA202',
            departure: 'London',
            arrival: 'Paris',
            departureTime: new Date('2024-06-02T09:00:00'),
            arrivalTime: new Date('2024-06-02T11:00:00'),
            price: 200
        },
        {
            id: 3,
            flightNumber: 'DL303',
            departure: 'Paris',
            arrival: 'Berlin',
            departureTime: new Date('2024-06-03T14:00:00'),
            arrivalTime: new Date('2024-06-03T16:00:00'),
            price: 150
        }
    ];
    addFlight(flight: FlightDTO): void {
        this.flights.push(flight);
    }

    getFlights(): FlightDTO[] {
        return this.flights;
    }
}
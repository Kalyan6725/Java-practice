package ui;

import dao.FlightDao;
import entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Scanner;
@Component
public class MyConsoleController {
    @Autowired
    private Scanner scanner;
    @Autowired
    private FlightDao flightDao;

    public void Menu() throws SQLException {
        while (true) {
            System.out.println("Enter Choice");
            System.out.println("1.add Flight");
            System.out.println("2.get flight by id");
            System.out.println("3.sort by departure date");
            System.out.println("4.sort by arrival date");
            System.out.println("5.sort by departure time");
            System.out.println("6.sort by arrival time");
            System.out.println("7.sort by departure date and time");
            System.out.println("8.sort by arrival date and time");
            int choice = scanner.nextInt();
            scanner.nextLine();
            redirectChoice(choice);
        }
    }
        
        void redirectChoice(int choice) throws SQLException {
            switch (choice){
                case 1:
                    addFlight();
                    break;
                case 2:
                    System.out.println("Enter Flight No:");
                    String flightNo=scanner.nextLine();
                    System.out.println(flightDao.getFlightByFlightNo(flightNo));
                    break;
                case 3:
                    flightDao.sortFlightsByDepartureDate();
                    break;
                case 4:
                    flightDao.sortFlightsByArrivalDate();
                    break;
                case 5:
                    flightDao.sortFlightsByDepartureTime();
                    break;
                case 6:
                    flightDao.sortFlightsByArrivalTime();
                    break;
                case 7:
                    flightDao.sortFlightsByDepartureDateAndTime();
                    break;
                case 8:
                    flightDao.sortFlightsByArrivalDateAndTime();
                    break;
            }
        }
        public void addFlight() throws SQLException {
            System.out.println("Enter Flight Details");
            System.out.println("Flight No:");
            String flightNo=scanner.nextLine();
            System.out.println("Airlines:");
            String airlines=scanner.nextLine();
            System.out.println("Departure Date:");
            String departureDate=scanner.nextLine();
            System.out.println("Arrival Date:");
            String arrivalDate=scanner.nextLine();
            System.out.println("Departure Time:");
            String departureTime=scanner.nextLine();
            System.out.println("Arrival Time:");
            String arrivalTime=scanner.nextLine();
            Flight flight=new Flight(flightNo,airlines, Date.valueOf(departureDate),Date.valueOf(arrivalDate),Time.valueOf(departureTime), Time.valueOf(arrivalTime));
            flightDao.addFlight(flight);
        }
    }



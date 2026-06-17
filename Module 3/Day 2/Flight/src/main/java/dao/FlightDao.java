package dao;

import entity.Flight;

import java.sql.SQLException;

public interface FlightDao {
    void addFlight(Flight flight) throws SQLException;
    Flight getFlightByFlightNo(String flightNo) throws SQLException;
    void sortFlightsByDepartureDate() throws SQLException;
    void sortFlightsByArrivalDate() throws SQLException;
    void sortFlightsByDepartureTime() throws SQLException;
    void sortFlightsByArrivalTime() throws SQLException;
    void sortFlightsByDepartureDateAndTime() throws SQLException;
    void sortFlightsByArrivalDateAndTime() throws SQLException;
}

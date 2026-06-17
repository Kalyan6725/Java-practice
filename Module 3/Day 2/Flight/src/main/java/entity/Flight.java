package entity;

import java.sql.Time;
import java.sql.Date;

public class Flight {
    private String FlightNo;
    private String Airlines;
    private Date DepartureDate;
    private Date ArrivalDate;
    private Time DepartureTime;
    private Time ArrivalTime;

    public Flight(String FlightNo, String Airlines, Date DepartureDate, Date ArrivalDate, Time DepartureTime, Time ArrivalTime) {
        this.FlightNo = FlightNo;
        this.Airlines = Airlines;
        this.DepartureDate = DepartureDate;
        this.ArrivalDate = ArrivalDate;
        this.DepartureTime = DepartureTime;
        this.ArrivalTime = ArrivalTime;
    }
    
    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getAirlines() {
        return Airlines;
    }

    public void setAirlines(String airlines) {
        Airlines = airlines;
    }

    public Date getDepartureDate() {
        return DepartureDate;
    }

    public void setDepartureDate(Date departureDate) {
        DepartureDate = departureDate;
    }

    public Date getArrivalDate() {
        return ArrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        ArrivalDate = arrivalDate;
    }

    public Time getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(Time departureTime) {
        DepartureTime = departureTime;
    }

    public Time getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String toString(){
        return "{Flight no:"+this.getFlightNo()+" Airlines:"+this.getAirlines()+" Departure Date:"+this.getDepartureDate()+" Arrival Date:"+this.getArrivalDate()+" Departure Time:"+this.getDepartureTime()+" Arrival Time:"+this.getArrivalTime()+"}";
    }
}
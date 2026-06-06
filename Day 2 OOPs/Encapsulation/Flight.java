class Flight{
    String airline;
    String flightnumber;
    String source;
    String destination;
    String departureDateTime;
    String arrivalDateTime;

    public Flight(String airline, String flightnumber, String source, String destination, String departureDateTime, String arrivalDateTime) {
        this.airline = airline;
        this.flightnumber = flightnumber;
        this.source = source;
        this.destination = destination;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }

    public void bookFlight(){
        System.out.println("Booking flight " + flightnumber + " from " + source + " to " + destination);
    }

    public void getStatus(){
        System.out.println("Flight "+flightnumber+" from "+source+" to "+destination+" is on time.");

    }

    public void showDetails(){
        System.out.println("Airline: " + airline);
        System.out.println("Flight Number: " + flightnumber);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Date/Time: " + departureDateTime);
        System.out.println("Arrival Date/Time: " + arrivalDateTime);
    }

}
class Hotel {
    String name;
    String location;
    int rating;

    Hotel(String name, String location, int rating) {
        this.name = name;
        this.location = location;
        this.rating = rating;
    }

    public void bookRoom() {
        System.out.println("Booking a room at " + name);
    }
    
    void displayInfo() {
        System.out.println("Hotel Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Rating: " + rating + " stars");
    }
}
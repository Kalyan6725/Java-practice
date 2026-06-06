class Restaurant {
    String name;
    String cuisine;
    double rating;

    public Restaurant(String name, String cuisine, double rating) {
        this.name = name;
        this.cuisine = cuisine;
        this.rating = rating;
    }
    void orderFood() {
        System.out.println("Ordering food from " + name);
    }
    
    public void displayInfo() {
        System.out.println("Restaurant Name: " + name);
        System.out.println("Cuisine: " + cuisine);
        System.out.println("Rating: " + rating);
    }
}
class Address{
    String street;
    String city;
    String country;

    public Address(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }
    public String toString() {
        return "Address [street=" + street + ", city=" + city + ", country=" + country + "]";
    }
    public void displayAddress() {
        System.out.println("Street: " + street);
        System.out.println("City: " + city);
        System.out.println("Country: " + country);
    }
}
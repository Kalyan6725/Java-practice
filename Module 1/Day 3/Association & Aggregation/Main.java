class Main{
    public static void main(String[] args) {
        Address address = new Address("123 Main St", "Anytown", "USA");
        Person person = new Person("John", "Doe", address);
        Passport passport = new Passport("A1234567", "USA", "2020-01-01", "2030-01-01");

        // establish bidirectional association
        person.setPassport(passport);

        System.out.println(person);
        System.out.println(passport);
    }
}
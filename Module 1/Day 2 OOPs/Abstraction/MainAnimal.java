class MainAnimal{
    public static void main(String[] args) {
        // Animal animal = new Lion(); // Upcasting Lion to Animal
        // animal.eat(); // This will call the eat method from Lion class due to method overriding 
        // animal.sleep(); // This will call the sleep method from Lion class due to method overriding
        // animal.talk(); // This will call the talk method from Animal class because Lion does not override it

        // Animal dog = new Dog(); // Upcasting Dog to Animal
        // dog.eat(); // This will call the eat method from Dog class due to method overriding 
        // dog.sleep(); // This will call the sleep method from Dog class due to method overriding
        // dog.talk(); // This will call the talk method from Animal class because Dog does not override it

        // Animal deer = new Deer(); // Upcasting Deer to Animal
        // deer.eat(); // This will call the eat method from Deer class due to method overriding
        // deer.sleep(); // This will call the sleep method from Deer class due to method overriding
        // deer.talk(); // This will call the talk method from Animal class because Deer does not override it
        // ((Deer) deer).run(); // Downcasting Deer to access the run method specific to Deer class

        Dog dog2 = new Dog(); // Creating an instance of Dog class
        dog2.petting(); // This will call the petting method from PetAnimal class because
        dog2.eat(); // This will call the eat method from Dog class due to method overriding
        dog2.sleep(); // This will call the sleep method from Dog class due to method overriding
        dog2.talk(); // This will call the talk method from Animal class because Dog does not override it
    }
}
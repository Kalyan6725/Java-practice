import java.util.Scanner;
class MainAnimal{
    public static void main(String[] args) {
        //upcasting
        Animal d = new Lion();
        d.eat(); // This will call the eat method from Lion class due to method overriding
        d.talk(); // This will call the talk method from Animal class because Lion does not override it
        d.sleep(); // This will call the sleep method from Lion class due to method overriding
        //d.roar(); // This will cause a compile-time error because roar is not defined in Animal class
        ((Lion) d).roar(); // This will call the roar method from Lion class due to downcasting

        // Animal a = new Dog();
        // a.eat(); // This will call the eat method from Dog class due to method overriding
        // a.sleep(); // This will call the sleep method from Dog class due to method overriding
        // a.talk(); // This will call the talk method from Animal class because Dog does not override it
        // //a.bark(); // This will cause a compile-time error because bark is not defined
        // ((Dog) a).bark(); // This will call the bark method from Dog class due to downcasting


        // Scanner sc=new Scanner(System.in);
        // System.out.println("Enter your choice:\n 1:Animal \n 2:Lion \n 3:Dog \n 4:Deer \n");
        // int choice=sc.nextInt();
        // Animal animal;
        // switch(choice){
        //     case 1:
        //         animal = new Animal();
        //         break;
        //     case 2:
        //         animal = new Lion();
        //         ((Lion) animal).roar();
        //         break;
        //     case 3:
        //         animal = new Dog();
        //         ((Dog) animal).bark();
        //         break;
        //     case 4:
        //         animal = new Deer();
        //         ((Deer) animal).run();
        //         //animal.run();
        //         break;
        //     default:
        //         System.out.println("Invalid choice");
        //         return;
        // }
        // animal.eat();
        // animal.sleep();
        // animal.talk();
    }
}
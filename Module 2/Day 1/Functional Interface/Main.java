public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        double time =nextInt("Enter time in 24-hour format: ");

        
        Greeting gm = new Greeting() {
            @Override
            public String greet(String name) {
                return "Good Morning, " + name;
            }
        };
        System.out.println(gm.greet("Alice"));
        
        System.out.println("----------------");


        
        Greeting gn = new Greeting() {
            @Override
            public String greet(String name) {
                return "Good Night, " + name;
            }
        };
        System.out.println(gn.greet("Bob"));
    }
}
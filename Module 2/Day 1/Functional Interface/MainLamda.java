public class MainLamda {
    public static void main(String[] args) {
        
        Greeting gm =()-> { //Lamda expression
                return "Good Morning, ";
        };
        
        System.out.println("----------------");


        
        Greeting gn =()-> { //Lamda expression
                return "Good Night, ";
        };
        System.out.println(gn.greet("Bob"));
    }
}
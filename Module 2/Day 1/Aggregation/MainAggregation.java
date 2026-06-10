import java.util.Scanner;
class MainAggregation{
    public static void main(String[] args){
        Employee e1=new Employee("Kalyan");
        Employee e2=new Employee("Nithish");

        Department d=new Department("IT","Chennai");
       
        d.addEmployee(e1);
        d.addEmployee(e2);
        d.getDetails();
        Scanner sc=new Scanner(System.in);
        System.out.println("enter index to delete:");
        int choice=sc.nextInt();
        d.deleteEmployee(choice);
        d.getDetails();



        //Player-Team Aggregation
        Player p1=new Player("Kalyan",24);
        Player p2=new Player("Nithish",24);
        Team t=new Team("Chennai Super Kings");
        t.addPlayer(p1);
        t.addPlayer(p2);
        System.out.println("Team Name: "+t.getTname());
        System.out.println("Players in the team:");
        System.out.println(t.getPlayers());
        
    }
}
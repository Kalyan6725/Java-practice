import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;

class MainQueue {
    public static void main(String[] args){
        Queue<Integer> q = new LinkedList<>();
        q.add(50);
        q.add(40);
        q.add(32);
        q.add(21);
        //Collections.sort(q); //Only works for List interface, not for Queue interface
        System.out.println(q);
        System.out.println(q.peek());
        System.out.println("Removed element: " + q.remove());
        System.out.println(q);

        Queue<String> strq = new LinkedList<>();
        strq.add("zdfgf");
        strq.add("gsd");
        strq.add("ahs");
        strq.add("tyj");
        //Collections.sort(q); //Only works for List interface, not for Queue interface
        System.out.println(strq);
        System.out.println(strq.peek());
        System.out.println("Removed element: " + strq.remove());
        System.out.println(strq);

        Queue<Integer> q2 = new PriorityQueue<>();
        q2.add(12);
        q2.add(6);
        q2.add(21);
        System.out.println(q2);
        System.out.println(q2.peek());
        q2.remove();
        System.out.println(q2);

        Queue<String> strQ = new PriorityQueue<>();
        strQ.add("Zbc");
        strQ.add("Mca");
        strQ.add("Cab");
        System.out.println(strQ);
        System.out.println(strQ.peek());
        strQ.remove();
        System.out.println(strQ);

        Queue<Person> p=new PriorityQueue<>(new Comparator<Person>(){
            public int compare(Person p1,Person p2){
                return p1.getAge()-p2.getAge();
            }
        });
        p.add(new Person("John", "Doe", 30));
        p.add(new Person("kals","sams",21));
        p.add(new Person("vjsd","fesdef",32));
        System.out.println(p);
        System.out.println(p.peek());
        p.remove();
        System.out.println(p);
    }
}
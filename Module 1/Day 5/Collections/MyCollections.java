import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
class MyCollections {
    public static void main(String[] args) {
        //List -->size not fixed, indexing is there
        List<Integer> listInt = new ArrayList<>();//Arraylist is contiguous memory allocation
        listInt.add(10);
        listInt.add(20);
        listInt.add(30);
        listInt.add(40);
        //listInt.set(4,50); //Index out of bound exception

        System.out.println(listInt);
        System.out.println(listInt.get(2));
        listInt.set(2, 35);
        listInt.remove(1);
        System.out.println(listInt);
        Collections.sort(listInt);
        System.out.println(listInt);

        List<Double> d=new ArrayList<>(); 
        d.add(10.0);
        d.add(20.0);
        d.add(12.2);
        d.add(15.6); 
        System.out.println(d);
        d.remove(2);
        System.out.println(d);
        d.set(1,50.23);
        System.out.println(d);

        List<String> Str=new LinkedList<>(); //LinkedList is non contiguous memory allocation
        Str.add("Kals");
        Str.add("sams");
        System.out.println(Str);
        System.out.println(Str.get(0));
        Str.remove(1);
        System.out.println(Str);

        List<Person> p=new ArrayList<>();
        p.add(new Person("John", "Doe", 30));
        p.add(new Person("kals","sams",21));
        p.add(new Person("vjsd","fesdef",32));
        System.out.println(p);
        Collections.sort(p,new Comparator<Person>(){
            public int compare(Person p1,Person p2){
                return p1.getAge()-p2.getAge();
            }
        });
        System.out.println(p);//As per age in ascending order

        Set<Person> s=new HashSet<>();
        s.add(new Person("kals","sams",21));
        s.add(new Person("kals","sams",21));
        s.add(new Person("kals","sams",21));
        s.add(new Person("kals","sams",21));
        System.out.println(s);

    }
}
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.Collections;
class SetMain{
    public static void main(String[] args) {
        //Set --> size not fixed, indexing is not there, no duplicates allowed
        Set<Integer> st=new HashSet<>(); //HashSet is non ordered and non sorted collection
         st.add(50);
         st.add(12);
         st.add(3);
         st.add(50);
         st.add(12);
         System.out.println(st);

        Set<Integer> st1=new LinkedHashSet<>(); //LinkedHashSet maintains the insertion order
         st1.add(50);
         st1.add(12);
         st1.add(3);
         st1.add(50);
         st1.add(12);
         System.out.println(st1);

        Set<Integer> st2=new TreeSet<>(); //TreeSet is sorted collection
         st2.add(50);
         st2.add(12);
         st2.add(3);
         st2.add(50);
         st2.add(12);
         System.out.println(st2);

        Set<String> str=new HashSet<>(); //HashSet is non ordered and non sorted collection
         str.add("50");
         str.add("12");
         str.add("3");
         str.add("50");
         str.add("12");
         System.out.println(str);

        Set<String> str2=new LinkedHashSet<>(); //LinkedHashSet maintains the insertion order
         str2.add("50");
         str2.add("12");
         str2.add("3");
         str2.add("50"); 
         System.out.println(str2);

        Set<String> str3=new TreeSet<>();//TreeSet is sorted collection
        str3.add("50");
        str3.add("12");
        str3.add("3");
        str3.add("50"); 
        str3.add("12");
        System.out.println(str3);

        Set<Person> p=new HashSet<>(); //HashSet is non ordered and non sorted collection
         p.add(new Person("John", "Doe", 30));
         p.add(new Person("Jane", "Smith", 25));
         p.add(new Person("Bob", "Johnson", 35));
         System.out.println(p);

        Set<Person> p2=new LinkedHashSet<>(); //LinkedHashSet maintains the insertion order
        p2.add(new Person("John", "Doe", 30));
        p2.add(new Person("Jane", "Smith", 25));
        p2.add(new Person("Bob", "Johnson", 35));
        System.out.println(p2);

        Set<Person> p3=new TreeSet<>(new Comparator<Person>(){
            public int compare(Person p1,Person p2){
                return p1.getAge()-p2.getAge();
            }
        });
        
        p3.add(new Person("John", "Doe", 30));
        p3.add(new Person("Jane", "Smith", 25));
        p3.add(new Person("Bob", "Johnson", 35));
        System.out.println(p3);
    }
}
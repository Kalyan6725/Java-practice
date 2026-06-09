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
         st.add(50); //duplicate element, will not be added to the set
         st.add(12);
         System.out.println(st);

         Set<Integer> p=new LinkedHashSet<>(); //LinkedHashSet is ordered but not sorted collection, it maintains the insertion order
         p.add(5);
         p.add(12);
         p.add(3);
         p.add(5); //duplicate element, will not be added to the set
         p.add(12);
         System.out.println(p);

        Set<Integer> t=new TreeSet<>();//TreeSet is sorted collection, it sorts the elements in natural order (ascending order for numbers)
        t.add(50);
        t.add(12);
        t.add(3);
        t.add(50); //duplicate element, will not be added to the set
        t.add(12);
        System.out.println(t);

    }
}
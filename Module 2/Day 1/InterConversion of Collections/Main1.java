import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class Main1 {
    public static void main(String[] args) {
        //ArrayList of Integers to LinkedHashSet and back to ArrayList
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(1);
        list.add(5);

        System.out.println("ArrayList: " + list);
        Set<Integer> set = new LinkedHashSet<>(list);
        System.out.println("Set: " + set);
        list = new ArrayList<>(set);
        System.out.println("New ArrayList: " + list);
        System.out.println("ArrayList: " + list);
        // Convert ArrayList to TreeSet
        Set<Integer> set = new TreeSet<>(list);
        System.out.println("Set: " + set);
        list = new ArrayList<>(set);
        System.out.println("New ArrayList: " + list);

        //ArrayList of Strings to Set and back to ArrayList
        List<String> list1 = new ArrayList<>();
        list1.add("Hello");
        list1.add("World");
        list1.add("Hello");
        list1.add("Java");
        System.out.println("ArrayList: " + list1);
        Set<String> set1 = new LinkedHashSet<>(list1);
        System.out.println("Set: " + set1);
        list1 = new ArrayList<>(set1);
        System.out.println("New ArrayList: " + list1);
    }
}
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

class Main3 {
    public static void main(String[] args) {
        //Arraylist to Hashset to treeset
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(1);
        list.add(5);
        System.out.println("ArrayList: " + list);
        Set<Integer> set = new HashSet<>(list);
        System.out.println("HashSet: " + set);
        Set<Integer> treeSet = new TreeSet<>(list);
        System.out.println("TreeSet: " + treeSet);
    }
}
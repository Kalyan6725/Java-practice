import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

class IteratorMain {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        list.add("!");

        Iterator<String> iterator = list.iterator();
        System.out.println(iterator.getClass().getName());
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("------------------------------");

        List<String> llist = new LinkedList<>();
        llist.add("Hello");
        llist.add("World");
        llist.add("!");

        Iterator<String> Literator = llist.iterator();
        System.out.println(Literator.getClass().getName());
        while (Literator.hasNext()) {
            System.out.println(Literator.next());
        }

        System.out.println("------------------------------");

        Set<String> set = new HashSet<>();
        set.add("Hello");
        set.add("World");
        set.add("!");
        Iterator<String> setIterator = set.iterator();
        System.out.println(setIterator.getClass().getName());
        while (setIterator.hasNext()) {
            System.out.println(setIterator.next());
        }


    }
}
import java.util.ArrayList;
import java.util.List;
class Collections {
    public static void main(String[] args) {
        //List -->size not fixed, indexing is there
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        System.out.println(list);
        System.out.println(list.get(2));
        list.set(2, 35);
        list.remove(1);
        System.out.println(list);
    }
}
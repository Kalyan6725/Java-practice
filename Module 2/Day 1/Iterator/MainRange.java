import java.util.Iterator;

public class MainRange {
    public static void main(String[] args) {
        MyRange range = new MyRange(1, 5);
        Iterator<Integer> itr=range.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        for (Integer num : range) {
            System.out.println(num);
        }
    }
}
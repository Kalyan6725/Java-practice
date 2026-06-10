import java.util.Iterator;
public class MyRange implements Iterable<Integer> {
    private int start;
    private int end;

    public MyRange(int start,int end){
        this.start=start;
        this.end=end;
    }

    @Override
    public Iterator<Integer> iterator(){
        return new MyRangeIterator(start,end);
    }
}